package noweekend.core.domain.user

import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.LocationRequest
import noweekend.core.api.controller.v1.request.ProfileRequest
import noweekend.core.api.controller.v1.request.TagUpdateRequest
import noweekend.core.api.controller.v1.response.OnboardingStatus
import noweekend.core.api.controller.v1.response.OnboardingStatusResponse
import noweekend.core.api.controller.v1.response.UserInformationResponse
import noweekend.core.domain.enumerate.ScheduleCategory
import noweekend.core.domain.schedule.ScheduleReader
import noweekend.core.domain.tag.BasicTag
import noweekend.core.domain.tag.TagReader
import noweekend.core.domain.tag.TagWriter
import noweekend.core.domain.tag.UserTags
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class UserServiceImpl(
    private val tagReader: TagReader,
    private val tagWriter: TagWriter,
    private val userWriter: UserWriter,
    private val userReader: UserReader,
    private val scheduleReader: ScheduleReader,
) : UserService {

    override fun getDefaultTag(): List<String> {
        return tagReader.getDefaultTags()
    }

    override fun getStateTags(userId: String): UserTags {
        return tagReader.getUserTags(userId)
    }

    override fun updateTag(request: TagUpdateRequest, userId: String) {
        val userTags = tagReader.getUserTags(userId)
        tagWriter.upsertTags(
            addScheduleTags = request.addScheduleTags,
            deleteScheduleTags = request.deleteScheduleTags,
            userTags = userTags,
            userId = userId,
        )
    }

    override fun registerSelectedBasicTag(basicTag: List<BasicTag>, userId: String) {
        tagWriter.registerSelectedBasicTag(basicTag, userId)
    }

    override fun upsertProfile(request: ProfileRequest, userId: String) {
        val birthLocalDate = parseLocalDate(request)
        val user = userReader.findUserById(userId) ?: throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL)
        val merged = user.copy(
            name = request.nickname,
            birthDate = birthLocalDate,
        )
        userWriter.upsert(merged)
    }

    override fun updateRemainingAnnualLeave(request: LeaveInputRequest, userId: String) {
        val user = userReader.findUserById(userId) ?: throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL)
        var daysToAdd = request.days.toDouble()
        if (request.hours == 4) {
            daysToAdd += 0.5
        }
        val updatedUser = user.copy(
            remainingAnnualLeave = (user.remainingAnnualLeave ?: 0.0) + daysToAdd,
        )
        userWriter.upsert(updatedUser)
    }

    private fun parseLocalDate(request: ProfileRequest): LocalDate {
        return LocalDate.parse(request.birthDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
    }

    override fun updateLocation(request: LocationRequest, userId: String) {
        val user = userReader.findUserById(userId) ?: throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL)
        val roundedLocation = Location.rounded(request.latitude, request.longitude)

        val updateUser = user.copy(
            location = roundedLocation,
        )
        userWriter.upsert(updateUser)
    }

    override fun getUserInformationById(userId: String): UserInformationResponse {
        val user = userReader.findUserById(userId) ?: throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL)

        val endDate = LocalDateTime.now()
        val startDate = endDate.minusDays(30)

        val schedules = scheduleReader.findSchedulesByUserIdAndDateRange(userId, startDate, endDate)

        val temperatureByDay = schedules.groupBy { it.startTime.toLocalDate() }

        val dailyTemperatures = temperatureByDay.map { (_, daySchedules) ->
            if (daySchedules.any { it.category == ScheduleCategory.LEAVE }) {
                0.0
            } else {
                val validTemperatures = daySchedules
                    .filter { it.completed }
                    .map { it.temperature }

                if (validTemperatures.isNotEmpty()) {
                    validTemperatures.average()
                } else {
                    null
                }
            }
        }.filterNotNull()

        val averageTemperature = if (dailyTemperatures.isNotEmpty()) {
            dailyTemperatures.average()
        } else {
            0.0
        }

        return UserInformationResponse.of(user, averageTemperature)
    }

    override fun getOnboardingStatus(userId: String): OnboardingStatusResponse {
        val user = userReader.findUserById(userId) ?: throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL)

        val nameAndBirthdayEntered = user.birthDate != null && user.name != null
        val annualLeaveEntered = user.remainingAnnualLeave != null
        val userTags = tagReader.getUserTags(userId)
        val selectedTagsCount = (userTags.selectedBasicTags + userTags.unselectedBasicTags + userTags.selectedCustomTags + userTags.unselectedCustomTags).count { it.selected }
        val tagEntered = selectedTagsCount > 0

        // 1. 이름/생년월일 안됨 -> NONE
        if (!nameAndBirthdayEntered && !annualLeaveEntered && !tagEntered) {
            return OnboardingStatusResponse(OnboardingStatus.NONE)
        }

        // 2. 이름/생년월일만 됨 -> NAME_AND_BIRTHDAY
        if (nameAndBirthdayEntered && !annualLeaveEntered && !tagEntered) {
            return OnboardingStatusResponse(OnboardingStatus.NAME_AND_BIRTHDAY)
        }

        // 3. 연차까지 됨(이름/생년월일, 연차 ok, 태그 없음) -> ANNUAL_LEAVE
        if (nameAndBirthdayEntered && annualLeaveEntered && !tagEntered) {
            return OnboardingStatusResponse(OnboardingStatus.ANNUAL_LEAVE)
        }

        // 4. 모든게 정상적으로 입력됨 -> DONE
        if (nameAndBirthdayEntered && annualLeaveEntered && tagEntered) {
            return OnboardingStatusResponse(OnboardingStatus.DONE)
        }

        throw CoreException(ErrorType.INVALID_ONBOARD_STATUS)
    }
}

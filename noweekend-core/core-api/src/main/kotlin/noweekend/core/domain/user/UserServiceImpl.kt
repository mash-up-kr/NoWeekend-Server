package noweekend.core.domain.user

import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.LocationRequest
import noweekend.core.api.controller.v1.request.ProfileRequest
import noweekend.core.api.controller.v1.request.TagUpdateRequest
import noweekend.core.domain.tag.BasicTag
import noweekend.core.domain.tag.TagReader
import noweekend.core.domain.tag.TagWriter
import noweekend.core.domain.tag.UserTags
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class UserServiceImpl(
    private val tagReader: TagReader,
    private val tagWriter: TagWriter,
    private val userWriter: UserWriter,
    private val userReader: UserReader,
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
            remainingAnnualLeave = user.remainingAnnualLeave + daysToAdd,
        )
        userWriter.upsert(updatedUser)
    }

    private fun parseLocalDate(request: ProfileRequest): LocalDate {
        return LocalDate.parse(request.birthDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
    }

    override fun updateLocation(request: LocationRequest, userId: String) {
        val user = userReader.findUserById(userId) ?: throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL)
        val updateUser = user.copy(
            location = Location(
                latitude = request.latitude,
                longitude = request.longitude,
            ),
        )
        userWriter.upsert(updateUser)
    }
}

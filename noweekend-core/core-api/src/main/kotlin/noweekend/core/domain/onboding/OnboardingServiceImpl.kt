package noweekend.core.domain.onboding

import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.OnboardingRequest
import noweekend.core.domain.schedule.ScheduleTag
import noweekend.core.domain.schedule.ScheduleWriter
import noweekend.core.domain.user.UserReader
import noweekend.core.domain.user.UserWriter
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class OnboardingServiceImpl(
    private val scheduleWriter: ScheduleWriter,
    private val userWriter: UserWriter,
    private val userReader: UserReader,
) : OnboardingService {
    override fun registerScheduleTage(scheduleTag: List<ScheduleTag>, userId: String) {
        scheduleWriter.registerTags(scheduleTag, userId)
    }

    override fun registerProfile(request: OnboardingRequest, userId: String) {
        val birthLocalDate = parseLocalDate(request)
        val user = userReader.findUserById(userId) ?: throw NoSuchElementException("id로 사용자를 찾을 수 없음")
        val merged = user.copy(
            name = request.nickname,
            birthDate = birthLocalDate,
        )
        userWriter.upsert(merged)
    }

    override fun registerRemainingAnnualLeave(request: LeaveInputRequest, userId: String) {
        val user = userReader.findUserById(userId) ?: throw NoSuchElementException("id로 사용자를 찾을 수 없음")
        var daysToAdd = request.days.toDouble()
        if (request.hours == 4) {
            daysToAdd += 0.5
        }
        val updatedUser = user.copy(
            remainingAnnualLeave = user.remainingAnnualLeave + daysToAdd,
        )
        userWriter.upsert(updatedUser)
    }

    private fun parseLocalDate(request: OnboardingRequest): LocalDate {
        return LocalDate.parse(request.birthDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
    }
}

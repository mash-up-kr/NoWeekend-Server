package noweekend.core.api.service.schedule

import noweekend.core.api.controller.v1.request.ScheduleCreateRequest
import noweekend.core.api.controller.v1.request.ScheduleUpdateRequest
import noweekend.core.api.controller.v1.response.DailyScheduleResponse
import noweekend.core.api.controller.v1.response.ScheduleResponse
import noweekend.core.domain.schedule.Schedule
import noweekend.core.domain.schedule.ScheduleReader
import noweekend.core.domain.schedule.ScheduleWriter
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

interface ScheduleApplicationService {
    fun getSchedulesByDateRange(
        userId: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyScheduleResponse>

    fun createSchedule(
        userId: String,
        request: ScheduleCreateRequest,
    ): ScheduleResponse

    fun updateSchedule(
        userId: String,
        scheduleId: String,
        request: ScheduleUpdateRequest,
    ): ScheduleResponse

    fun deleteSchedule(userId: String, scheduleId: String)
}

@Service
class ScheduleApplicationServiceImpl(
    private val scheduleReader: ScheduleReader,
    private val scheduleWriter: ScheduleWriter,
) : ScheduleApplicationService {

    override fun getSchedulesByDateRange(
        userId: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyScheduleResponse> {
        val startDateTime = startDate.atStartOfDay()
        val endDateTime = endDate.atTime(LocalTime.MAX)

        val schedules = scheduleReader.findSchedulesByUserIdAndDateRange(userId, startDateTime, endDateTime)

        val schedulesByDate = schedules.groupBy { it.startTime.toLocalDate() }

        val result = mutableListOf<DailyScheduleResponse>()
        var currentDate = startDate

        while (!currentDate.isAfter(endDate)) {
            val dailySchedules = schedulesByDate[currentDate] ?: emptyList()

            result.add(
                DailyScheduleResponse(
                    date = currentDate,
                    dailyTemperature = dailySchedules.sumOf { if (it.completed) it.temperature else 0 },
                    schedules = dailySchedules.map { it.toResponse() },
                ),
            )
            currentDate = currentDate.plusDays(1)
        }

        return result
    }

    override fun createSchedule(
        userId: String,
        request: ScheduleCreateRequest,
    ): ScheduleResponse {
        val startDateTime = if (request.allDay) {
            request.date.atStartOfDay()
        } else {
            request.date.atTime(request.startTime ?: LocalTime.of(9, 0))
        }

        val endDateTime = if (request.allDay) {
            request.date.atTime(LocalTime.MAX)
        } else {
            request.date.atTime(request.endTime ?: LocalTime.of(18, 0))
        }

        val schedule = Schedule.newSchedule(
            userId = userId,
            title = request.title,
            startTime = startDateTime,
            endTime = endDateTime,
            category = request.category,
            temperature = request.temperature,
            allDay = request.allDay,
            alarmOption = request.alarmOption,
        )

        val savedSchedule = scheduleWriter.save(schedule)
        return savedSchedule.toResponse()
    }

    override fun updateSchedule(
        userId: String,
        scheduleId: String,
        request: ScheduleUpdateRequest,
    ): ScheduleResponse {
        val existingSchedule = scheduleReader.findScheduleById(scheduleId)
            ?: throw CoreException(ErrorType.NOT_FOUND_ERROR, "Schedule not found: $scheduleId")

        if (existingSchedule.userId != userId) {
            throw CoreException(ErrorType.FORBIDDEN_ERROR, "You don't have permission to update this schedule")
        }

        val originalDate = existingSchedule.startTime.toLocalDate()
        val startDateTime = if (request.allDay) {
            originalDate.atStartOfDay()
        } else {
            originalDate.atTime(request.startTime)
        }

        val endDateTime = if (request.allDay) {
            originalDate.atTime(LocalTime.MAX)
        } else {
            originalDate.atTime(request.endTime)
        }

        val updatedSchedule = existingSchedule.copy(
            title = request.title,
            startTime = startDateTime,
            endTime = endDateTime,
            category = request.category,
            temperature = request.temperature,
            allDay = request.allDay,
            alarmOption = request.alarmOption,
        )

        val savedSchedule = scheduleWriter.update(updatedSchedule)
        return savedSchedule.toResponse()
    }

    override fun deleteSchedule(userId: String, scheduleId: String) {
        val existingSchedule = scheduleReader.findScheduleById(scheduleId)
            ?: throw CoreException(ErrorType.NOT_FOUND_ERROR, "Schedule not found: $scheduleId")

        if (existingSchedule.userId != userId) {
            throw CoreException(ErrorType.FORBIDDEN_ERROR, "You don't have permission to delete this schedule")
        }

        scheduleWriter.deleteById(scheduleId)
    }

    private fun Schedule.toResponse(): ScheduleResponse {
        return ScheduleResponse(
            id = this.id,
            title = this.title,
            startTime = this.startTime,
            endTime = this.endTime,
            category = this.category,
            temperature = this.temperature,
            allDay = this.allDay,
            alarmOption = this.alarmOption,
            completed = this.completed,
        )
    }
}

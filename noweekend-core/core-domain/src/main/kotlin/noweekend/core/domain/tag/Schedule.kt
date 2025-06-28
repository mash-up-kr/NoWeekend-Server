package noweekend.core.domain.tag

import noweekend.core.domain.enumerate.AlarmOption
import noweekend.core.domain.enumerate.ScheduleCategory
import noweekend.core.domain.util.IdGenerator
import java.time.LocalDateTime

data class Schedule(
    val id: String,
    val userId: String,
    val title: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val category: ScheduleCategory,
    val temperature: Int,
    val allDay: Boolean,
    val alarmOption: AlarmOption,
    val completed: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun newSchedule(
            userId: String,
            title: String,
            startTime: LocalDateTime,
            endTime: LocalDateTime,
            category: ScheduleCategory,
            temperature: Int,
            allDay: Boolean,
            alarmOption: AlarmOption,
        ): Schedule {
            return Schedule(
                id = IdGenerator.generate(),
                userId = userId,
                title = title,
                startTime = startTime,
                endTime = endTime,
                category = category,
                temperature = temperature,
                allDay = allDay,
                alarmOption = alarmOption,
                completed = false,
                createdAt = null,
                updatedAt = null,
            )
        }
    }
}

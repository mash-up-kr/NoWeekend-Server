package noweekend.core.domain.schedule

import java.time.LocalDateTime

interface ScheduleRepository {
    fun findScheduleById(id: String): Schedule?
    fun findSchedulesByUserIdAndDateRange(userId: String, startDate: LocalDateTime, endDate: LocalDateTime): List<Schedule>
    fun save(schedule: Schedule): Schedule
    fun update(schedule: Schedule): Schedule
    fun deleteById(id: String)
}

package noweekend.core.domain.tag

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class ScheduleReader(
    private val scheduleRepository: ScheduleRepository,
) {
    fun findScheduleById(id: String): Schedule? {
        return scheduleRepository.findScheduleById(id)
    }

    fun findSchedulesByUserIdAndDateRange(
        userId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<Schedule> {
        return scheduleRepository.findSchedulesByUserIdAndDateRange(userId, startDate, endDate)
    }
}

package noweekend.core.domain.schedule

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class ScheduleWriter(
    private val scheduleRepository: ScheduleRepository,
) {
    fun save(schedule: Schedule): Schedule {
        return scheduleRepository.save(schedule)
    }

    fun update(schedule: Schedule): Schedule {
        return scheduleRepository.update(schedule)
    }

    fun deleteById(id: String) {
        scheduleRepository.deleteById(id)
    }
}

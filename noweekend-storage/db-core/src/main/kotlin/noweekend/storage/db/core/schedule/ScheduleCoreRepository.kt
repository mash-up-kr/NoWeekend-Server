package noweekend.storage.db.core.schedule

import noweekend.core.domain.schedule.Schedule
import noweekend.core.domain.schedule.ScheduleRepository
import org.springframework.stereotype.Repository

@Repository
class ScheduleCoreRepository(
    private val scheduleJpaRepository: ScheduleJpaRepository,
) : ScheduleRepository {
    override fun register(schedule: Schedule) {
        scheduleJpaRepository.save(schedule.toEntity())
    }
}

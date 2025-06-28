package noweekend.storage.db.core.schedule

import noweekend.core.domain.schedule.Schedule
import noweekend.core.domain.schedule.ScheduleRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ScheduleCoreRepository(
    private val jpaRepository: ScheduleJpaRepository,
    private val queryDslRepository: ScheduleQueryDslRepository,
) : ScheduleRepository {
    override fun findScheduleById(id: String): Schedule? {
        return jpaRepository.findById(id)
            .map { it.toSchedule() }
            .orElse(null)
    }

    override fun findSchedulesByUserIdAndDateRange(
        userId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<Schedule> {
        return queryDslRepository.findSchedulesByUserIdAndDateRange(userId, startDate, endDate)
            .map { it.toSchedule() }
    }

    override fun save(schedule: Schedule): Schedule {
        val scheduleEntity = schedule.toEntity()
        val savedEntity = jpaRepository.save(scheduleEntity)
        return savedEntity.toSchedule()
    }

    override fun update(schedule: Schedule): Schedule {
        val scheduleEntity = schedule.toEntity()
        val updatedEntity = jpaRepository.save(scheduleEntity)
        return updatedEntity.toSchedule()
    }

    override fun deleteById(id: String) {
        jpaRepository.deleteById(id)
    }
}

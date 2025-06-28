package noweekend.core.domain.schedule

interface ScheduleRepository {
    fun register(schedule: Schedule)
}

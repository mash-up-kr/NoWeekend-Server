package noweekend.core.domain.schedule

import noweekend.core.domain.util.IdGenerator

data class Schedule(
    val id: String,
    val tag: ScheduleTag,
    val userId: String,
) {
    companion object {
        fun register(tag: ScheduleTag, userId: String): Schedule {
            return Schedule(
                id = IdGenerator.generate(),
                tag = tag,
                userId = userId,
            )
        }
    }
}

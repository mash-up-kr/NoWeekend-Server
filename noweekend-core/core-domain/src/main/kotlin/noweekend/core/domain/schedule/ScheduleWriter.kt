package noweekend.core.domain.schedule

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class ScheduleWriter(
    private val scheduleRepository: ScheduleRepository,
) {
    fun registerTags(tags: List<ScheduleTag>, userId: String) {
        tags.stream().forEach {
                tag ->
            scheduleRepository.register(
                Schedule.register(tag, userId),
            )
        }
    }
}

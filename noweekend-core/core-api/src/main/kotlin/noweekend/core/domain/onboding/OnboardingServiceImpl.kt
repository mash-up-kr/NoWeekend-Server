package noweekend.core.domain.onboding

import noweekend.core.domain.schedule.ScheduleTag
import noweekend.core.domain.schedule.ScheduleWriter
import org.springframework.stereotype.Service

@Service
class OnboardingServiceImpl(
    private val scheduleWriter: ScheduleWriter,
) : OnboardingService {
    override fun registerScheduleTage(scheduleTag: List<ScheduleTag>, userId: String) {
        scheduleWriter.registerTags(scheduleTag, userId)
    }
}

package noweekend.core.domain.onboding

import noweekend.core.domain.schedule.ScheduleTag

interface OnboardingService {
    fun registerScheduleTage(scheduleTag: List<ScheduleTag>, userId: String)
}

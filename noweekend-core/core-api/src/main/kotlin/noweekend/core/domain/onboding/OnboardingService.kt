package noweekend.core.domain.onboding

import noweekend.core.api.controller.v1.request.OnboardingRequest
import noweekend.core.domain.schedule.ScheduleTag

interface OnboardingService {
    fun registerScheduleTage(scheduleTag: List<ScheduleTag>, userId: String)
    fun registerProfile(request: OnboardingRequest, userId: String)
}

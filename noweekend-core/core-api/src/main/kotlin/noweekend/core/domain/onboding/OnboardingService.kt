package noweekend.core.domain.onboding

import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.OnboardingRequest
import noweekend.core.domain.schedule.ScheduleTag

interface OnboardingService {
    fun registerScheduleTag(scheduleTag: List<ScheduleTag>, userId: String)
    fun registerProfile(request: OnboardingRequest, userId: String)
    fun registerRemainingAnnualLeave(request: LeaveInputRequest, userId: String)
}

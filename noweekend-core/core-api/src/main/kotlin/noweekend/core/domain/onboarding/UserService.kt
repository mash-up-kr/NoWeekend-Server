package noweekend.core.domain.onboarding

import noweekend.core.api.controller.v1.request.EditNickname
import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.OnboardingRequest
import noweekend.core.domain.schedule.ScheduleTag

interface UserService {
    fun registerScheduleTag(scheduleTag: List<ScheduleTag>, userId: String)
    fun registerProfile(request: OnboardingRequest, userId: String)
    fun updateRemainingAnnualLeave(request: LeaveInputRequest, userId: String)
    fun updateNickname(editNickname: EditNickname, userId: String)
}

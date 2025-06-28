package noweekend.core.domain.onboarding

import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.ProfileRequest
import noweekend.core.domain.schedule.BasicTag

interface UserService {
    fun registerSelectedBasicTag(basicTag: List<BasicTag>, userId: String)
    fun upsertProfile(request: ProfileRequest, userId: String)
    fun updateRemainingAnnualLeave(request: LeaveInputRequest, userId: String)
    fun getDefaultTag(): List<String>
}

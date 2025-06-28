package noweekend.core.domain.onboarding

import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.ProfileRequest
import noweekend.core.api.controller.v1.request.TagRequest
import noweekend.core.domain.schedule.BasicTag
import noweekend.core.domain.schedule.UserTags

interface UserService {
    fun registerSelectedBasicTag(basicTag: List<BasicTag>, userId: String)
    fun upsertProfile(request: ProfileRequest, userId: String)
    fun updateRemainingAnnualLeave(request: LeaveInputRequest, userId: String)
    fun getDefaultTag(): List<String>
    fun updateTag(request: TagRequest, userId: String)
    fun getStateTags(userId: String): UserTags
}

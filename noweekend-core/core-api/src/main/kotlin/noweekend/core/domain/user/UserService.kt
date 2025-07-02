package noweekend.core.domain.user

import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.LocationRequest
import noweekend.core.api.controller.v1.request.ProfileRequest
import noweekend.core.api.controller.v1.request.TagUpdateRequest
import noweekend.core.domain.tag.BasicTag
import noweekend.core.domain.tag.UserTags

interface UserService {
    fun registerSelectedBasicTag(basicTag: List<BasicTag>, userId: String)
    fun upsertProfile(request: ProfileRequest, userId: String)
    fun updateRemainingAnnualLeave(request: LeaveInputRequest, userId: String)
    fun getDefaultTag(): List<String>
    fun updateTag(request: TagUpdateRequest, userId: String)
    fun getStateTags(userId: String): UserTags
    fun updateLocation(request: LocationRequest, userId: String)
}

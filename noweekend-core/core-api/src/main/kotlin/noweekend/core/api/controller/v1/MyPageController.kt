package noweekend.core.api.controller.v1

import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.ProfileRequest
import noweekend.core.api.controller.v1.request.TagUpdateRequest
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.domain.onboarding.UserService
import noweekend.core.domain.tag.UserTags
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class MyPageController(
    private val userService: UserService,
) : MyPageControllerDocs {
    @PatchMapping("/profile")
    override fun updateProfile(
        @CurrentUserId userId: String,
        @Validated @RequestBody profileRequest: ProfileRequest,
    ): ApiResponse<String> {
        userService.upsertProfile(profileRequest, userId)
        return ApiResponse.success(
            "프로필이 성공적으로 변경 되었습니다.",
        )
    }

    @PatchMapping("/leave")
    override fun updateLeave(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: LeaveInputRequest,
    ): ApiResponse<String> {
        userService.updateRemainingAnnualLeave(request, userId)
        return ApiResponse.success(
            "연차 정보가 성공적으로 변경되었습니다.",
        )
    }

    @GetMapping("/tags")
    override fun getTags(
        @CurrentUserId userId: String,
    ): ApiResponse<UserTags> {
        return ApiResponse.success(
            userService.getStateTags(userId),
        )
    }

    @PatchMapping("/tags")
    override fun updateTags(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: TagUpdateRequest,
    ): ApiResponse<String> {
        userService.updateTag(request, userId)
        return ApiResponse.success(
            "태그들이 성공적으로 변경되었습니다.",
        )
    }
}

package noweekend.core.api.controller.v1

import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.ProfileRequest
import noweekend.core.api.controller.v1.request.TagRequest
import noweekend.core.api.controller.v1.response.DefaultTags
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.domain.user.UserService
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class OnboardingController(
    private val userService: UserService,
) : OnboardingControllerDocs {
    @GetMapping("/onboarding/tag")
    override fun getDefaultTag(
        @CurrentUserId userId: String,
    ): ApiResponse<DefaultTags> {
        return ApiResponse.success(
            DefaultTags(userService.getDefaultTag()),
        )
    }

    @PostMapping("/onboarding/tag")
    override fun registerSelectedDefaultTag(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: TagRequest,
    ): ApiResponse<String> {
        val scheduleTags = request.validatedScheduleTags()
        userService.registerSelectedBasicTag(scheduleTags, userId)
        return ApiResponse.success(
            "일정 등록이 성공적으로 완료되었습니다.",
        )
    }

    @PostMapping("/onboarding/profile")
    override fun submitProfile(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: ProfileRequest,
    ): ApiResponse<String> {
        userService.upsertProfile(request, userId)
        return ApiResponse.success(
            "닉네임 및 생년월일 등록이 성공적으로 완료되었습니다.",
        )
    }

    @PostMapping("/onboarding/leave")
    override fun submitLeave(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: LeaveInputRequest,
    ): ApiResponse<String> {
        userService.updateRemainingAnnualLeave(request, userId)
        return ApiResponse.success(
            "연차 정보가 성공적으로 저장되었습니다.",
        )
    }
}

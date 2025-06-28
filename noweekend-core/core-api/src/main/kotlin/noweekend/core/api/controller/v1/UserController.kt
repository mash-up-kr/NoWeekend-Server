package noweekend.core.api.controller.v1

import noweekend.core.api.controller.v1.request.EditNickname
import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.OnboardingRequest
import noweekend.core.api.controller.v1.request.ScheduleRequest
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.domain.mypage.MyPageService
import noweekend.core.domain.onboarding.OnboardingService
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val onboardingService: OnboardingService,
    private val myPageService: MyPageService,
) : UserControllerDocs {
    @PostMapping("/onboarding/schedules")
    override fun saveUserSchedules(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: ScheduleRequest,
    ): ApiResponse<String> {
        val scheduleTags = request.validatedScheduleTags()
        onboardingService.registerScheduleTag(scheduleTags, userId)
        return ApiResponse.success(
            "일정 등록이 성공적으로 완료되었습니다.",
        )
    }

    @PostMapping("/onboarding/profile")
    override fun submitProfile(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: OnboardingRequest,
    ): ApiResponse<String> {
        onboardingService.registerProfile(request, userId)
        return ApiResponse.success(
            "닉네임 및 생년월일 등록이 성공적으로 완료되었습니다.",
        )
    }

    @PostMapping("/onboarding/leave")
    override fun submitLeave(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: LeaveInputRequest,
    ): ApiResponse<String> {
        onboardingService.registerRemainingAnnualLeave(request, userId)
        return ApiResponse.success(
            "연차 정보가 성공적으로 저장되었습니다.",
        )
    }

    @PatchMapping("/edit/profile")
    override fun updateNickname(
        @CurrentUserId userId: String,
        @Validated @RequestBody editNickname: EditNickname,
    ) {
        myPageService.updateNickname(editNickname, userId)
    }
}

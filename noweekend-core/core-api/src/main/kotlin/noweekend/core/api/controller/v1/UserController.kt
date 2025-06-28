package noweekend.core.api.controller.v1

import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.OnboardingRequest
import noweekend.core.api.controller.v1.request.ScheduleRequest
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.domain.onboding.OnboardingService
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user/onboarding")
class UserController(
    private val onboardingService: OnboardingService,
) : UserControllerDocs {
    @PostMapping("/schedules")
    override fun saveUserSchedules(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: ScheduleRequest,
    ): ApiResponse<String> {
        val scheduleTags = request.validatedScheduleTags()
        onboardingService.registerScheduleTage(scheduleTags, userId)
        return ApiResponse.success(
            "일정 등록이 성공적으로 완료되었습니다.",
        )
    }

    @PostMapping("/profile")
    override fun submitProfile(
        @RequestBody request: OnboardingRequest,
    ): ApiResponse<String> {
        return ApiResponse.success(
            "닉네임 및 생년월일 등록이 성공적으로 완료되었습니다.",
        )
    }

    @PostMapping("/leave")
    override fun submitLeave(
        @Validated @RequestBody request: LeaveInputRequest,
    ): ApiResponse<String> {
        return ApiResponse.success(
            "연차 정보가 성공적으로 저장되었습니다.",
        )
    }
}

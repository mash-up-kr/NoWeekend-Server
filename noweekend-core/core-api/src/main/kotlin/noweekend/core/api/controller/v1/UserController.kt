package noweekend.core.api.controller.v1

import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.core.api.controller.v1.request.OnboardingRequest
import noweekend.core.api.controller.v1.request.ScheduleRequest
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "온보딩", description = "온보딩 등록 API")
@RestController
@RequestMapping("/api/v1/user/onboarding")
class UserController() : UserControllerDocs {
    @PostMapping("/schedules")
    override fun saveUserSchedules(
        @Validated @RequestBody request: ScheduleRequest,
    ): ApiResponse<String> {
        val enumList = request.validatedScheduleTags()
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
}

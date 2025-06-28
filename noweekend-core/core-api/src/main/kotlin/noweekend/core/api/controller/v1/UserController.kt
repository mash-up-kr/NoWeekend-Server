package noweekend.core.api.controller.v1

import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.core.api.controller.v1.request.ScheduleRequest
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "유저 컨트롤러", description = "유저 관련 API")
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
}

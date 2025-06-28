package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "온보딩 시 닉네임/생년월일 등록 DTO")
data class OnboardingRequest(
    @field:NotBlank(message = "닉네임은 필수입니다.")
    @field:Size(max = 6, message = "닉네임은 6자 이하여야 합니다.")
    @field:Schema(
        description = "닉네임(최대 6글자, 한글/영문 구분 없음)",
        example = "김매송",
    )
    val nickname: String,

    @field:ValidBirthDate
    val birthDate: String,
)

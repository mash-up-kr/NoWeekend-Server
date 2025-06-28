package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Schema(description = "온보딩 시 닉네임/생년월일 등록 DTO")
data class OnboardingRequest(
    @field:NotBlank(message = "닉네임은 필수입니다.")
    @field:Schema(
        description = "닉네임",
        example = "김매송.."
    )
    val nickname: String,

    @field:NotBlank(message = "생년월일은 필수입니다.")
    @field:Pattern(
        regexp = "^[0-9]{8}$",
        message = "생년월일은 8자리 숫자(yyyyMMdd)여야 합니다."
    )
    @field:Schema(
        description = "생년월일(8자리, yyyyMMdd)",
        example = "19991213"
    )
    val birthDate: String,
)

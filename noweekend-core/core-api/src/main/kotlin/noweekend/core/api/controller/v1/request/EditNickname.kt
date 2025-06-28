package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "닉네임 수정 요청 DTO")
data class EditNickname(
    @field:NotBlank(message = "닉네임은 필수입니다.")
    @field:Size(max = 6, message = "닉네임은 6자 이하여야 합니다.")
    @field:Schema(
        description = "새 닉네임(최대 6글자, 한글/영문 구분 없음)",
        example = "뉴닉네임",
    )
    val nickname: String,
)

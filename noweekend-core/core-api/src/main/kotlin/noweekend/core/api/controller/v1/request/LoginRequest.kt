package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "구글 로그인 요청 DTO")
data class LoginRequest(
    @field:NotBlank(message = "accessToken은 필수입니다.")
    @Schema(description = "구글 OAuth AccessToken", example = "ya29.a0Af...")
    val accessToken: String,

    @Schema(description = "사용자 이름 (선택)", example = "홍길동")
    val name: String?,
)

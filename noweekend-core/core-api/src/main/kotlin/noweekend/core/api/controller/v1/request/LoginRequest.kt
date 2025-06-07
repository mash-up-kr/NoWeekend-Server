package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "구글 로그인 요청 DTO")
data class LoginRequest(
    @field:NotBlank(message = "accessToken은 필수입니다.")
    @field:Schema(description = "구글 OAuth AccessToken", example = "ya29.a0Af...")
    val accessToken: String,

    @field:Schema(description = "사용자 이름. 회원가입시 필수, 로그인시 null로 보내시면됩니다.", example = "홍길동")
    val name: String?,
)

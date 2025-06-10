package noweekend.core.api.controller.v1.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "소셜 로그인 응답 DTO")
data class OAuthLoginResponse(
    @field:Schema(description = "이미 가입된 회원 여부")
    val exists: Boolean,
    @field:Schema(description = "발급된 액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
    val accessToken: String,
)

package noweekend.core.api.controller.v1.request

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "accessToken은 필수입니다.") val accessToken: String,
    val name: String?,
)

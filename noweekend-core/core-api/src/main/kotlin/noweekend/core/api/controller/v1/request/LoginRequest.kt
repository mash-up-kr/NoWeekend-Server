package noweekend.core.api.controller.v1.request

data class LoginRequest(
    val accessToken: String,
    val name: String?,
)

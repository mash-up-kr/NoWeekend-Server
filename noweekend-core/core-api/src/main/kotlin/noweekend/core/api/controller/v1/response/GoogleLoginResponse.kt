package noweekend.core.api.controller.v1.response

data class GoogleLoginResponse(
    val email: String,
    val exists: Boolean,
    val accessToken: String,
)

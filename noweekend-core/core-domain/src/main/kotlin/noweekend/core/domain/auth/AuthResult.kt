package noweekend.core.domain.auth

data class AuthResult(
    val accessToken: String,
    val exists: Boolean,
)

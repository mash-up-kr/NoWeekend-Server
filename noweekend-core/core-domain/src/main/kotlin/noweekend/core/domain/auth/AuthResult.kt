package noweekend.core.domain.auth

data class AuthResult(
    val email: String,
    val exists: Boolean,
)

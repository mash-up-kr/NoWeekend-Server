package noweekend.core.api.security.jwt

interface JwtProvider {
    fun validate(token: String): Boolean
    fun getUserIdFromToken(token: String): Long
    fun generate(userId: Long): String
}

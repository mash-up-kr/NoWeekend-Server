package noweekend.core.domain.jwt

interface JwtProvider {
    fun validate(token: String): Boolean
    fun getUserIdFromToken(token: String): String
    fun generate(userId: String): String
}

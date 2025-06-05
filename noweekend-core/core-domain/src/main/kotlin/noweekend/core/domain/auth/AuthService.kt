package noweekend.core.domain.auth

import noweekend.core.domain.jwt.JwtProvider
import noweekend.core.domain.user.ProviderType
import noweekend.core.domain.user.Role
import noweekend.core.domain.user.User
import noweekend.core.domain.user.UserReader
import noweekend.core.domain.user.UserWriter
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userWriter: UserWriter,
    private val userReader: UserReader,
    private val jwtProvider: JwtProvider,
) {
    fun googleLogin(email: String, name: String?): AuthResult {
        val user = userReader.findUserProviderAndEmail(ProviderType.GOOGLE, email)
            ?: return registerAndRespond(email, name)
        return respondWithToken(
            userId = user.id,
            exists = true,
        )
    }

    private fun registerAndRespond(email: String, name: String?): AuthResult {
        require(!name.isNullOrBlank()) { "신규 회원가입 시 이름(name) 정보가 필요합니다." }
        val newUser = User.newUser(
            email = email,
            name = name,
            providerType = ProviderType.GOOGLE,
            role = Role.USER,
        )
        userWriter.register(
            id = newUser.id,
            email = newUser.email,
            name = newUser.name,
            providerType = newUser.providerType,
            role = newUser.role,
        )
        return respondWithToken(
            userId = newUser.id,
            exists = false,
        )
    }

    private fun respondWithToken(userId: String, exists: Boolean): AuthResult {
        val accessToken = jwtProvider.generate(userId)
        return AuthResult(
            accessToken = accessToken,
            exists = exists,
        )
    }
}

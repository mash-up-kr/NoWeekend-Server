package noweekend.core.domain.auth

import noweekend.core.domain.jwt.JwtProvider
import noweekend.core.domain.user.ProviderType
import noweekend.core.domain.user.Role
import noweekend.core.domain.user.User
import noweekend.core.domain.user.UserReader
import noweekend.core.domain.user.UserWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val userWriter: UserWriter,
    private val userReader: UserReader,
    private val jwtProvider: JwtProvider,
) {

    @Transactional
    fun socialLogin(
        providerType: ProviderType,
        providerId: String,
        name: String?,
        revocableToken: String?,
    ): AuthResult {
        val user = userReader.findUserProviderAndProviderId(providerType, providerId)
            ?: return registerAndRespond(providerType, providerId, null, name, revocableToken)
        return respondWithToken(
            userId = user.id,
            exists = true,
        )
    }

    private fun registerAndRespond(
        providerType: ProviderType,
        providerId: String,
        email: String?,
        name: String?,
        revocableToken: String?,
    ): AuthResult {
        require(!name.isNullOrBlank()) { "신규 회원가입 시 이름(name) 정보가 필요합니다." }
        val newUser = User.newUser(
            email = email,
            name = name,
            providerType = providerType,
            providerId = providerId,
            revocableToken = revocableToken,
            role = Role.USER,
        )
        userWriter.register(newUser)
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

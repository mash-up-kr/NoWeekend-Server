package noweekend.core.domain.auth

import noweekend.core.domain.enumerate.ProviderType
import noweekend.core.domain.enumerate.Role
import noweekend.core.domain.jwt.JwtProvider
import noweekend.core.domain.user.User
import noweekend.core.domain.user.UserReader
import noweekend.core.domain.user.UserWriter
import org.slf4j.LoggerFactory
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
        email: String?,
        name: String?,
        revocableToken: String?,
    ): AuthResult {
        userReader.findUserProviderAndProviderId(providerType, providerId)?.let { user ->
            return respondWithToken(user.id, exists = true)
        }

        if (!email.isNullOrBlank()) {
            userReader.findUserByEmail(email)?.let { existing ->
                try {
                    userWriter.linkProvider(existing.id, providerType, providerId, revocableToken)
                } catch (e: Exception) {
                    logger.error(e.printStackTrace().toString())
                }
                return respondWithToken(existing.id, exists = true)
            }
        }

        return registerAndRespond(providerType, providerId, email, name, revocableToken)
    }

    private fun registerAndRespond(
        providerType: ProviderType,
        providerId: String,
        email: String?,
        name: String?,
        revocableToken: String?,
    ): AuthResult {
        require(!email.isNullOrBlank()) { "신규 회원가입 시 이메일 정보가 필요합니다." }
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
        return respondWithToken(newUser.id, exists = false)
    }

    private fun respondWithToken(userId: String, exists: Boolean): AuthResult {
        val accessToken = jwtProvider.generate(userId)
        return AuthResult(accessToken = accessToken, exists = exists)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthService::class.java)
    }
}

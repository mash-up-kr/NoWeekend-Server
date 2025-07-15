package noweekend.core.api.service.user

import noweekend.client.oauth.common.OAuthClient
import noweekend.client.oauth.common.Revocable
import noweekend.core.domain.user.UserReader
import noweekend.core.domain.user.UserWriter
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType
import org.springframework.stereotype.Service

interface UserApplicationService {
    fun deleteUser(userId: String)
}

@Service
class UserApplicationServiceImpl(
    private val userReader: UserReader,
    private val userWriter: UserWriter,
    private val authClients: List<OAuthClient>,
) : UserApplicationService {

    override fun deleteUser(userId: String) {
        try {
            val user = userReader.findUserById(userId) ?: throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL)
            val oauthClient = authClients.firstOrNull { client -> client.supports(user.providerType) }
            if (oauthClient is Revocable && !user.revocableToken.isNullOrBlank()) {
                oauthClient.revokeToken(user.revocableToken!!)
            }
            userWriter.delete(userId)
        } catch (_: NoSuchElementException) {
            throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL)
        }
    }
}

package noweekend.core.domain.auth

import noweekend.client.common.OAuthClient
import noweekend.client.common.OAuthLoginParams
import noweekend.core.api.controller.v1.response.OAuthLoginResponse
import org.springframework.stereotype.Service

@Service
class AuthApplicationServiceImpl(
    private val oauthClients: List<OAuthClient>,
    private val authService: AuthService,
) : AuthApplicationService {

    override fun signInForSocial(params: OAuthLoginParams, name: String?): OAuthLoginResponse {
        val oauthClient = oauthClients.firstOrNull { client -> client.supports(params.getProviderType()) }
            ?: throw IllegalStateException("provider type: ${params.getProviderType()} is not supported.")
        val authInfo = oauthClient.requestOAuthInfo(params)
        val authResult = authService.socialLogin(
            providerType = authInfo.getProviderType(),
            providerId = authInfo.getProviderId(),
            name = name,
            revocableToken = authInfo.getProviderRevocableToken(),
        )
        return OAuthLoginResponse(exists = authResult.exists, accessToken = authResult.accessToken)
    }
}

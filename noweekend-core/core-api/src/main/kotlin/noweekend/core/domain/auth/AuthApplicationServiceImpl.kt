package noweekend.core.domain.auth

import noweekend.client.oauth.common.OAuthClient
import noweekend.client.oauth.common.OAuthLoginParams
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
            email = authInfo.getEmail(),
            name = name,
            revocableToken = authInfo.getProviderRevocableToken(),
        )
        return OAuthLoginResponse(exists = authResult.exists, accessToken = authResult.accessToken)
    }
}

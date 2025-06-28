package noweekend.client.google

import noweekend.client.common.OAuthClient
import noweekend.client.common.OAuthInfo
import noweekend.client.common.OAuthLoginParams
import noweekend.client.properties.GoogleAuthProperties
import noweekend.core.domain.enumerate.ProviderType
import org.springframework.stereotype.Component

@Component
class GoogleClient internal constructor(
    private val googleTokenApi: GoogleTokenApi,
    private val googleResourceApi: GoogleResourceApi,
    private val googleAuthProperties: GoogleAuthProperties,
) : OAuthClient {

    override fun supports(providerType: ProviderType): Boolean {
        return providerType == ProviderType.GOOGLE
    }

    override fun requestOAuthInfo(params: OAuthLoginParams): OAuthInfo {
        val accessToken = requestAccessToken(params)
        return requestAuthInfo(accessToken)
    }

    private fun requestAccessToken(params: OAuthLoginParams): String {
        return googleTokenApi.getGoogleToken(
            code = params.getCode(),
            redirectUri = DEFAULT_REDIRECT_URI,
            clientId = googleAuthProperties.clientId,
            clientSecret = googleAuthProperties.clientSecret,
            grantType = GOOGLE_AUTHORIZATION_TYPE,
        ).accessToken
    }

    private fun requestAuthInfo(accessToken: String): GoogleOAuthInfo {
        return googleResourceApi.getUserInfo(accessToken)
    }

    companion object {
        private const val GOOGLE_AUTHORIZATION_TYPE = "authorization_code"
        private const val DEFAULT_REDIRECT_URI = "postmessage"
    }
}

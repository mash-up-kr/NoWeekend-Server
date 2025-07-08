package noweekend.core.domain.auth

import noweekend.client.oauth.common.OAuthLoginParams
import noweekend.core.api.controller.v1.response.OAuthLoginResponse

interface AuthApplicationService {
    fun signInForSocial(params: OAuthLoginParams, name: String?): OAuthLoginResponse
}

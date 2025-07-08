package noweekend.client.oauth.google

import noweekend.client.oauth.common.OAuthLoginParams
import noweekend.core.domain.enumerate.ProviderType

class GoogleLoginParams(private val code: String) : OAuthLoginParams {
    override fun getProviderType(): ProviderType = ProviderType.GOOGLE
    override fun getCode(): String = code
}

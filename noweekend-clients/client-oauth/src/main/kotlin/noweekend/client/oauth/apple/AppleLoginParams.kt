package noweekend.client.oauth.apple

import noweekend.client.oauth.common.OAuthLoginParams
import noweekend.core.domain.enumerate.ProviderType

class AppleLoginParams(private val code: String) : OAuthLoginParams {
    override fun getProviderType(): ProviderType = ProviderType.APPLE
    override fun getCode(): String = code
}

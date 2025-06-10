package noweekend.client.apple

import noweekend.client.common.OAuthLoginParams
import noweekend.core.domain.user.ProviderType

class AppleLoginParams(private val code: String) : OAuthLoginParams {
    override fun getProviderType(): ProviderType = ProviderType.APPLE
    override fun getCode(): String = code
}

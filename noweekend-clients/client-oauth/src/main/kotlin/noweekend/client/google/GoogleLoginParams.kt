package noweekend.client.google

import noweekend.client.common.OAuthLoginParams
import noweekend.core.domain.user.ProviderType

class GoogleLoginParams(private val code: String) : OAuthLoginParams {
    override fun getProviderType(): ProviderType = ProviderType.GOOGLE
    override fun getCode(): String = code
}

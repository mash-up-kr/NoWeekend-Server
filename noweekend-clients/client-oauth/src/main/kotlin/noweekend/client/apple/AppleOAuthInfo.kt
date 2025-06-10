package noweekend.client.apple

import noweekend.client.common.OAuthInfo
import noweekend.core.domain.user.ProviderType

class AppleOAuthInfo(
    private val id: String,
    private val revocableToken: String,
) : OAuthInfo {
    override fun getProviderType(): ProviderType = ProviderType.APPLE

    override fun getProviderId(): String = id

    override fun getProviderRevocableToken(): String = revocableToken
}

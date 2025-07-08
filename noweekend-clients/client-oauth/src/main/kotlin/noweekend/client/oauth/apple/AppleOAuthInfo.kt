package noweekend.client.oauth.apple

import noweekend.client.oauth.common.OAuthInfo
import noweekend.core.domain.enumerate.ProviderType

class AppleOAuthInfo(
    private val id: String,
    private val revocableToken: String,
) : OAuthInfo {
    override fun getProviderType(): ProviderType = ProviderType.APPLE

    override fun getProviderId(): String = id

    override fun getProviderRevocableToken(): String = revocableToken
}

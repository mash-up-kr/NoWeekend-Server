package noweekend.client.common

import noweekend.core.domain.user.ProviderType

interface OAuthClient {
    fun supports(providerType: ProviderType): Boolean
    fun requestOAuthInfo(params: OAuthLoginParams): OAuthInfo
}

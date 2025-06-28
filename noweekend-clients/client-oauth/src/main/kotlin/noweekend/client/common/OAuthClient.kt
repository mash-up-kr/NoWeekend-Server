package noweekend.client.common

import noweekend.core.domain.enumerate.ProviderType

interface OAuthClient {
    fun supports(providerType: ProviderType): Boolean
    fun requestOAuthInfo(params: OAuthLoginParams): OAuthInfo
}

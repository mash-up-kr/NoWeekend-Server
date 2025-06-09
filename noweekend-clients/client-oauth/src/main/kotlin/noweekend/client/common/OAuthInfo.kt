package noweekend.client.common

import noweekend.core.domain.user.ProviderType

interface OAuthInfo {
    fun getProviderType(): ProviderType
    fun getProviderId(): String
    fun getProviderRevocableToken(): String?
}

package noweekend.client.oauth.common

import noweekend.core.domain.enumerate.ProviderType

interface OAuthInfo {
    fun getProviderType(): ProviderType
    fun getProviderId(): String
    fun getProviderRevocableToken(): String?
    fun getEmail(): String?
}

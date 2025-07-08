package noweekend.client.oauth.common

import noweekend.core.domain.enumerate.ProviderType

interface OAuthLoginParams {
    fun getProviderType(): ProviderType
    fun getCode(): String
}

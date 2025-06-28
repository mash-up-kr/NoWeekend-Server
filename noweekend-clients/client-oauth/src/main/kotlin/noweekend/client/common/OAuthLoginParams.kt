package noweekend.client.common

import noweekend.core.domain.enumerate.ProviderType

interface OAuthLoginParams {
    fun getProviderType(): ProviderType
    fun getCode(): String
}

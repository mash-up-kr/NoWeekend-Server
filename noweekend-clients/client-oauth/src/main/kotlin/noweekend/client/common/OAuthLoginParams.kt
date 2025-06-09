package noweekend.client.common

import noweekend.core.domain.user.ProviderType

interface OAuthLoginParams {
    fun getProviderType(): ProviderType
    fun getCode(): String
}

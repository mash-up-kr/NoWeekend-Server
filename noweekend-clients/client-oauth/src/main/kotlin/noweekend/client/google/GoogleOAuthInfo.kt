package noweekend.client.google

import com.fasterxml.jackson.annotation.JsonProperty
import noweekend.client.common.OAuthInfo
import noweekend.core.domain.user.ProviderType

data class GoogleOAuthInfo(
    @JsonProperty("id")
    val id: String,

    @JsonProperty("email")
    val email: String?,
) : OAuthInfo {
    override fun getProviderId(): String = id
    override fun getProviderRevocableToken(): String? = null
    override fun getProviderType(): ProviderType = ProviderType.GOOGLE
}

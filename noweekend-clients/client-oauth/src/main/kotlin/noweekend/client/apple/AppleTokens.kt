package noweekend.client.apple

import com.fasterxml.jackson.annotation.JsonProperty

data class AppleTokens(
    @JsonProperty("access_token")
    val accessToken: String,

    @JsonProperty("expires_in")
    val expiresIn: String,

    @JsonProperty("id_token")
    val idToken: String,

    @JsonProperty("refresh_token")
    val refreshToken: String,

    @JsonProperty("token_type")
    val tokenType: String,

    @JsonProperty("error")
    val error: String? = null,
)

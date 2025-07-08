package noweekend.client.oauth.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth.google")
data class GoogleAuthProperties(
    val clientId: String,
    val clientSecret: String,
)

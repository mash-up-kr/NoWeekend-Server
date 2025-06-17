package noweekend.client.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "oauth.apple")
data class AppleAuthProperties(
    val appId: String,
    val keyId: String,
    val teamId: String,
    val secretKeyFilePath: String,
)

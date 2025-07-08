package noweekend.client.mcp.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kma")
data class KmaApiProperties(
    val key: String,
)

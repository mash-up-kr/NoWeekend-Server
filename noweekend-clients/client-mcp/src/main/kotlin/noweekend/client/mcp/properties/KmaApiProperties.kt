package noweekend.client.mcp.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kma.api")
class KmaApiProperties {
    lateinit var key: String
}

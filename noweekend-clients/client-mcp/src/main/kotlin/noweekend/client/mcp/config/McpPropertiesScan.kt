package noweekend.client.mcp.config

import noweekend.client.mcp.properties.KmaApiProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    KmaApiProperties::class,
)
class McpPropertiesScan

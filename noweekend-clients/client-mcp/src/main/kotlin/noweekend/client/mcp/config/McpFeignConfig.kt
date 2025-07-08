package noweekend.client.mcp.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(
    basePackages = [
        "noweekend.client.mcp.weather",
        "noweekend.client.mcp.holiday",
    ],
)
@Configuration
internal class McpFeignConfig

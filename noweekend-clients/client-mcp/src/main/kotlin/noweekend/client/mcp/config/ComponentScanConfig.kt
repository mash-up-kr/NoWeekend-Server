package noweekend.client.mcp.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ComponentScan(
    basePackages = [
        "noweekend.client.mcp.weather",
        "noweekend.client.mcp.holiday",
    ],
)
@Configuration("mcpClientsConfig")
class ComponentScanConfig

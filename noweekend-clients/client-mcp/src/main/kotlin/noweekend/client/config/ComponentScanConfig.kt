package noweekend.client.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ComponentScan(
    basePackages = [
        "noweekend.client.weather",
        "noweekend.client.holiday",
    ],
)
@Configuration("mcpClientsConfig")
class ComponentScanConfig

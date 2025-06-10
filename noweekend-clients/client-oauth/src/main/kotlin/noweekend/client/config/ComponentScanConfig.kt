package noweekend.client.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ComponentScan(
    basePackages = [
        "noweekend.client.google",
        "noweekend.client.apple",
        "noweekend.client.common",
    ],
)
@Configuration("clientsConfig")
class ComponentScanConfig

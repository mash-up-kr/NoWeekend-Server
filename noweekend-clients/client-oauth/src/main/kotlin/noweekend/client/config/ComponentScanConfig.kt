package noweekend.client.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ComponentScan(
    basePackages = [
        "noweekend.client.google",
    ],
)
@Configuration("clientsConfig")
class ComponentScanConfig

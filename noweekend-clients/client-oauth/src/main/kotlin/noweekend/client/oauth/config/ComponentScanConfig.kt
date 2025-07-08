package noweekend.client.oauth.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ComponentScan(
    basePackages = [
        "noweekend.client.oauth.google",
        "noweekend.client.oauth.apple",
        "noweekend.client.oauth.common",
    ],
)
@Configuration("clientsConfig")
class ComponentScanConfig

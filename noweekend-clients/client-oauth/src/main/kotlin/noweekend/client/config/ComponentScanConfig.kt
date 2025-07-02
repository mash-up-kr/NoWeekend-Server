package noweekend.client.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@ComponentScan(
    basePackages = [
        "noweekend.client.google",
        "noweekend.client.apple",
        "noweekend.client.common",
    ],
)
@Import(noweekend.core.domain.config.JasyptConfig::class)
@Configuration("clientsConfig")
class ComponentScanConfig

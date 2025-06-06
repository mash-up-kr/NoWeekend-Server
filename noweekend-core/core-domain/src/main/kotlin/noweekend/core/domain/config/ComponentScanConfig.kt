package noweekend.core.domain.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ComponentScan(
    basePackages = [
        "noweekend.core",
    ],
)
@Configuration("coreDomainConfig")
class ComponentScanConfig

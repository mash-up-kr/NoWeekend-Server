package noweekend.storage.db.core.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ComponentScan(
    basePackages = [
        "noweekend.storage.db.core",
    ],
)
@Configuration("coreDbConfig")
class ComponentScanConfig

package noweekend.client.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(
    basePackages = [
        "noweekend.client.weather",
    ],
)
@Configuration
internal class McpFeignConfig

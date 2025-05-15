package noweekend.mcpserver.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["noweekend.mcpserver.client"])
class FeignClientConfig {
}
package noweekend.client

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration("ChatFeignConfig")
@EnableFeignClients(basePackages = ["noweekend.client.api"])
internal class FeignConfig

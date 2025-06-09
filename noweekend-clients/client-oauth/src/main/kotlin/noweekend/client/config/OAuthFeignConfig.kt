package noweekend.client.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(
    basePackages = [
        "noweekend.client.google",
//        "noweekend.client.apple",
    ],
)
@Configuration
internal class OAuthFeignConfig

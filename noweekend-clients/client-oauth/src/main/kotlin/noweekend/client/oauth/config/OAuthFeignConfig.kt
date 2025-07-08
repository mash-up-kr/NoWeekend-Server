package noweekend.client.oauth.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(
    basePackages = [
        "noweekend.client.oauth.google",
        "noweekend.client.oauth.apple",
    ],
)
@Configuration
internal class OAuthFeignConfig

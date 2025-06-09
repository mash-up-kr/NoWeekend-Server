package noweekend.client.config

import noweekend.client.properties.AppleAuthProperties
import noweekend.client.properties.GoogleAuthProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    AppleAuthProperties::class,
    GoogleAuthProperties::class,
)
class OAuthConfig

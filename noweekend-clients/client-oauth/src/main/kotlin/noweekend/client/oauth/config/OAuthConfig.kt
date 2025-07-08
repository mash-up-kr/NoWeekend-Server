package noweekend.client.oauth.config

import noweekend.client.oauth.properties.AppleAuthProperties
import noweekend.client.oauth.properties.GoogleAuthProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    AppleAuthProperties::class,
    GoogleAuthProperties::class,
)
class OAuthConfig

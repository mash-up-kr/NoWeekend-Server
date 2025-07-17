package noweekend.mcphost.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        val converter = MappingJackson2HttpMessageConverter().apply {
            supportedMediaTypes = listOf(
                MediaType.APPLICATION_JSON,
                MediaType.TEXT_XML,
                MediaType.APPLICATION_XML,
                MediaType.TEXT_PLAIN,
                MediaType.ALL,
            )
        }
        return RestTemplate().apply {
            messageConverters = listOf<HttpMessageConverter<*>>(converter)
        }
    }
}

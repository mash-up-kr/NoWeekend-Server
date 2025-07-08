package noweekend.client.mcp.config

import feign.codec.Decoder
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter

@Configuration
class FeignXmlConfig {
    @Bean
    fun feignDecoder(): Decoder {
        val converters = mutableListOf<HttpMessageConverter<*>>()
        converters.add(MappingJackson2XmlHttpMessageConverter())
        return SpringDecoder { HttpMessageConverters(converters) }
    }
}

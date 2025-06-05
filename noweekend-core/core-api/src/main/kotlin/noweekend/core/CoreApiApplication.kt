package noweekend.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@ConfigurationPropertiesScan
@SpringBootApplication
@ComponentScan(
    basePackages = [
        "noweekend.core.api",
        "noweekend.core.domain",
        "noweekend.client",
    ],
)
class CoreApiApplication

fun main(args: Array<String>) {
    runApplication<CoreApiApplication>(*args)
}

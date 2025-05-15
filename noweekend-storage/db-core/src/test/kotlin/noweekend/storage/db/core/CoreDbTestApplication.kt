package noweekend.storage.db.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class CoreDbTestApplication

fun main(args: Array<String>) {
    runApplication<CoreDbTestApplication>(*args)
}

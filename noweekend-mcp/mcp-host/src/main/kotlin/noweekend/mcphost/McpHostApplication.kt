package noweekend.mcphost

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class McpHostApplication

fun main(args: Array<String>) {
    runApplication<McpHostApplication>(*args)
}

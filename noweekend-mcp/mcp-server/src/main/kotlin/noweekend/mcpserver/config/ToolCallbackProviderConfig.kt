package noweekend.mcpserver.config

import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import noweekend.mcpserver.tool.FutureWeatherTool

@Configuration
class ToolCallbackProviderConfig {

    @Bean
    fun getFutureWeatherProvider(futureWeatherTool: FutureWeatherTool): ToolCallbackProvider =
        MethodToolCallbackProvider.builder()
            .toolObjects(futureWeatherTool)
            .build()
}
package noweekend.mcphost.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatConfig {

    @Bean
    fun chatClient(
        chatClientBuilder: ChatClient.Builder,
        tools: ToolCallbackProvider,
    ): ChatClient =
        chatClientBuilder
            .defaultTools(tools)
            .build()
}

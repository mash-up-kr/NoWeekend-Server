package noweekend.mcphost.service

import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service

@Service
class ChatbotService(
    private val chatClient: ChatClient,
) {

    fun chat(question: String): String {
        return chatClient.prompt()
            .user(question)
            .call()
            .content()
            ?: throw IllegalStateException("Chat response content is null for question: $question")
    }

    fun chatWeatherPrompt(question: String): String {
        val formatInstruction = """
            Return a JSON array named "forecast". Each object must have these fields: \
            "date" (YYYY-MM-DD), "temp_max_c", "temp_min_c", "precip_mm", \
            "rain_chance_pct", "description". Do not include any markdown syntax \
            (#, *, -, etc.) or other special characters—only output plain JSON.
        """.trimIndent()

        return chatClient.prompt()
            .system(formatInstruction)
            .user(question)
            .call()
            .content()
            ?: throw IllegalStateException("ChatWeather response content is null for question: $question")
    }
}

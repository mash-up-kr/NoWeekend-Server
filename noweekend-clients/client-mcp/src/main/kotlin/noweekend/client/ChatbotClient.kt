package noweekend.client

import noweekend.client.api.ChatbotApi
import noweekend.client.model.ChatRequest
import noweekend.client.model.ChatResponse
import org.springframework.stereotype.Component

@Component
class ChatbotClient(
    private val api: ChatbotApi,
) {
    fun chat(question: String): ChatResponse {
        return api.chat(ChatRequest(question))
    }

    fun getFutureWeather(question: String): ChatResponse {
        return api.getFutureWeather(ChatRequest(question))
    }
}

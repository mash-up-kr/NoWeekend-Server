package noweekend.mcphost.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import noweekend.mcphost.controller.request.Tag
import noweekend.mcphost.controller.request.TagRequest
import noweekend.mcphost.controller.request.WeatherRequest
import noweekend.mcphost.controller.response.WeatherResponse
import noweekend.mcphost.service.Prompt.Companion.TAG_PROMPT
import noweekend.mcphost.service.Prompt.Companion.WEATHER_PROMPT
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId

@Service
class ChatbotService(
    private val chatClient: ChatClient,
    private val objectMapper: ObjectMapper,
) {

    fun chat(question: String): String {
        return chatClient.prompt()
            .user(question)
            .call()
            .content()
            ?: throw IllegalStateException("Chat response content is null for question: $question")
    }

    fun weatherRecommendation(request: WeatherRequest): List<WeatherResponse> {
        val baseDate = LocalDate.now(ZoneId.of("Asia/Seoul")).toString()
        val userMsg = """
            latitude: ${request.latitude}
            longitude: ${request.longitude}
            baseDate: $baseDate
        """.trimIndent()

        // 최대 2회까지 재시도
        repeat(2) { attempt ->
            val jsonString = chatClient.prompt()
                .system(WEATHER_PROMPT)
                .user(userMsg)
                .call()
                .content()
            try {
                if (jsonString != null) {
                    return objectMapper.readValue(jsonString)
                }
            } catch (e: Exception) {
                if (attempt == 1) {
                    throw IllegalStateException("현재 날씨 정보를 받아올 수 없습니다.")
                }
            }
        }
        throw IllegalStateException("현재 날씨 정보를 받아올 수 없습니다.")
    }

    fun tagRecommendation(request: TagRequest): List<Tag> {
        val tagJson = objectMapper.writeValueAsString(request.userTag)
        val userMsg = """
                Here is the user's tag lists in JSON:
                $tagJson
        """.trimIndent()

        repeat(2) { attempt ->
            val jsonString = chatClient.prompt()
                .system(TAG_PROMPT)
                .user(userMsg)
                .call()
                .content()
            try {
                if (jsonString != null) {
                    return objectMapper.readValue(jsonString)
                }
            } catch (e: Exception) {
                if (attempt == 1) throw IllegalStateException("태그 추천을 받아올 수 없습니다.")
            }
        }
        throw IllegalStateException("태그 추천을 받아올 수 없습니다.")
    }
}

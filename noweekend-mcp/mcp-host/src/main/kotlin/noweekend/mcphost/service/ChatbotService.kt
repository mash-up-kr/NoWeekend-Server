package noweekend.mcphost.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import noweekend.mcphost.controller.request.Tag
import noweekend.mcphost.controller.request.TagRequest
import noweekend.mcphost.controller.request.WeatherRequest
import noweekend.mcphost.controller.response.WeatherResponse
import noweekend.mcphost.service.Prompt.Companion.TAG_PROMPT
import noweekend.mcphost.service.Prompt.Companion.WEATHER_PROMPT
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId

@Service
class ChatbotService(
    private val chatClient: ChatClient,
    private val objectMapper: ObjectMapper,
) {

    private val logger = LoggerFactory.getLogger(ChatbotService::class.java)

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

        val backoff = listOf(1000L, 2000L, 4000L, 8000L, 16000L) // 1, 2, 4, 8, 16초 대기
        var lastException: Exception? = null

        for (attempt in backoff.indices) {
            try {
                val jsonString = chatClient.prompt()
                    .system(WEATHER_PROMPT)
                    .user(userMsg)
                    .call()
                    .content()
                if (jsonString != null) {
                    return objectMapper.readValue(jsonString)
                }
            } catch (e: Exception) {
                lastException = e
                if (!e.message.orEmpty().contains("Overloaded", ignoreCase = true)) {
                    throw IllegalStateException("현재 날씨 정보를 받아올 수 없습니다: ${e.message}", e)
                }
                if (attempt < backoff.lastIndex) {
                    Thread.sleep(backoff[attempt])
                }
            }
        }
        throw IllegalStateException("현재 날씨 정보를 받아올 수 없습니다: ${lastException?.message}", lastException)
    }

    fun tagRecommendation(request: TagRequest): List<Tag> {
        val tagJson = objectMapper.writeValueAsString(request.userTag)
        val userMsg = """
                Here is the user's tag lists in JSON:
                $tagJson
        """.trimIndent()

        repeat(5) { attempt ->
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

    fun tagRecommendationOnlyNew(request: TagRequest): List<Tag> {
        val tagJson = objectMapper.writeValueAsString(request.userTag)
        val userMsg = """
            Here is the user's tag lists in JSON:
            $tagJson
        """.trimIndent()

        repeat(5) { attempt ->
            val jsonString = chatClient.prompt()
                .system(Prompt.ONLY_NEW_TAG_PROMPT)
                .user(userMsg)
                .call()
                .content()
            try {
                logger.info("LLM tagRecommendOnlyNew raw response: $jsonString")

                if (jsonString != null) {
                    val tags: List<Tag> = objectMapper.readValue(jsonString)
                    val allOldTags = (
                        request.userTag.selectedBasicTags + request.userTag.unselectedBasicTags +
                            request.userTag.selectedCustomTags + request.userTag.unselectedCustomTags
                        ).map { it.content }.toSet()
                    require(tags.all { it.content !in allOldTags }) { "추천 결과에 기존 태그가 포함됨" }
                    return tags
                }
            } catch (e: Exception) {
                if (attempt == 1) throw IllegalStateException("태그 추천을 받아올 수 없습니다.")
            }
        }
        throw IllegalStateException("태그 추천을 받아올 수 없습니다.")
    }
}

package noweekend.mcphost.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import noweekend.mcphost.controller.request.Tag
import noweekend.mcphost.controller.request.TagRequest
import noweekend.mcphost.controller.request.WeatherRequest
import noweekend.mcphost.controller.response.WeatherResponse
import noweekend.mcphost.service.Prompt.Companion.TAG_SYSTEM_PROMPT
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

        val maxRetries = 5
        val backoff = listOf(1000L, 2000L, 4000L, 8000L, 16000L) // 1,2,4,8,16초 대기
        var lastException: Exception? = null

        for (attempt in 0 until maxRetries) {
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
                if (attempt < maxRetries - 1) {
                    Thread.sleep(backoff.getOrElse(attempt) { backoff.last() })
                }
            }
        }
        throw IllegalStateException("현재 날씨 정보를 받아올 수 없습니다: ${lastException?.message}", lastException)
    }

    fun tagRecommendation(request: TagRequest): List<Tag> {
        val tagJson = objectMapper.writeValueAsString(request.userTag)

        val userPrompt = """
Here is the user's tag lists in JSON:
$tagJson

Based on the above rules, return ONLY a valid JSON array of 3 Korean lifestyle activity tags. Do NOT output any other text, explanations, or markdown.
        """.trimIndent()

        var lastError: Exception? = null

        repeat(7) { attempt ->
            try {
                val rawResponse = chatClient.prompt()
                    .system(TAG_SYSTEM_PROMPT)
                    .user(userPrompt)
                    .call()
                    .content()

                if (rawResponse.isNullOrBlank()) {
                    throw IllegalArgumentException("Claude 응답이 null 또는 빈 문자열입니다.")
                }

                // ✅ 마크다운 제거 (정확한 쌍따옴표 닫힘 포함)
                val cleaned = rawResponse
                    .removePrefix("```json")
                    .removePrefix("```")
                    .removeSuffix("```")
                    .trim()

                if (cleaned.isBlank() || !cleaned.trim().startsWith("[")) {
                    throw IllegalStateException("응답이 JSON 배열이 아님: $cleaned")
                }

                val nodes = objectMapper.readTree(cleaned)
                if (!nodes.isArray || nodes.size() != 3) {
                    throw IllegalArgumentException("응답이 3개의 태그로 구성된 JSON 배열이 아님")
                }

                return objectMapper.readValue(cleaned)
            } catch (e: Exception) {
                lastError = e
            }
        }

        throw IllegalStateException("태그 추천을 받아올 수 없습니다: ${lastError?.message}", lastError)
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

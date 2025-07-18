package noweekend.mcphost.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import noweekend.mcphost.controller.Prompt
import noweekend.mcphost.controller.request.AiGenerateVacationRequest
import noweekend.mcphost.controller.request.AiVacationContent
import noweekend.mcphost.controller.request.AiVacationResponse
import noweekend.mcphost.controller.request.AiVacationTitle
import noweekend.mcphost.controller.request.Tag
import noweekend.mcphost.controller.request.TagRequest
import noweekend.mcphost.controller.request.WeatherRequest
import noweekend.mcphost.controller.response.WeatherResponse
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId

@Service
class ChatbotService(
    private val chatClient: ChatClient,
    private val objectMapper: ObjectMapper,
    private val prompt: Prompt,
) {

    private val logger = LoggerFactory.getLogger(ChatbotService::class.java)

    fun weatherRecommendation(request: WeatherRequest): List<WeatherResponse> {
        val baseDate = LocalDate.now(ZoneId.of("Asia/Seoul")).toString()
        val userMsg = """
        latitude: ${request.latitude}
        longitude: ${request.longitude}
        baseDate: $baseDate
        """.trimIndent()

        val maxRetries = 5
        val backoff = listOf(1000L, 2000L, 4000L, 8000L, 16000L)
        var lastException: Exception? = null

        for (attempt in 0 until maxRetries) {
            try {
                val jsonString = chatClient.prompt()
                    .system(prompt.weatherPrompt)
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
                    .system(prompt.tagSystemPrompt)
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
                .system(prompt.onlyNewTagSystemPrompt)
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
            } catch (_: Exception) {
                if (attempt == 4) throw IllegalStateException("태그 추천을 받아올 수 없습니다.")
            }
        }
        throw IllegalStateException("태그 추천을 받아올 수 없습니다.")
    }

    private val chunkSize = 2

    fun generateVacationContent(request: AiGenerateVacationRequest): AiVacationContent {
        val allDates: List<String> = generateSequence(request.startDate) { prev ->
            if (prev < request.endDate) prev.plusDays(1) else null
        }
            .map(LocalDate::toString)
            .toList()

        val usedItems = mutableListOf<String>()

        val itineraryChunks = allDates
            .chunked(chunkSize)
            .mapIndexed { idx, datesChunk ->
                val detailPrompt = prompt.detailedPlanPrompt(
                    request,
                    datesChunk,
                    idx * chunkSize,
                    allDates.size,
                    usedItems,
                )
                val userJson = objectMapper.writeValueAsString(
                    mapOf(
                        "dates" to datesChunk,
                        "profile" to minimalProfileMap(request),
                        "totalDays" to allDates.size,
                    ),
                )
                val resp = chatClient.prompt()
                    .system(detailPrompt)
                    .user(userJson)
                    .call()
                val content = resp.content() ?: error("Empty chunk at index $idx")
                usedItems += extractUsedItems(content)
                content
            }

        val planRaw = itineraryChunks.joinToString("\n\n")
        return AiVacationContent(content = planRaw)
    }

    private fun minimalProfileMap(req: AiGenerateVacationRequest) = mapOf(
        "travelStyle" to req.chosenTravelStyleLabel,
        "activityType" to req.chosenActivityTypeLabel,
        "restPreference" to req.chosenRestPreferenceLabel,
        "leisureInterest" to req.chosenLeisurePreferenceLabel,
        "preferredTags" to req.selectedTags,
        "excludedTags" to req.unselectedTags,
    )

    private fun extractUsedItems(chunkRaw: String): List<String> {
        val items = mutableListOf<String>()
        val commonRegex = """• [^:]+: ([^,]+)""".toRegex()
        items += commonRegex.findAll(chunkRaw).map { it.groupValues[1] }.toList()
        val hotelRegex = """• Night / Accommodation: ([^,]+)""".toRegex()
        items += hotelRegex.findAll(chunkRaw).map { it.groupValues[1] }.toList()
        return items
    }

    private fun printRaw(text: String) {
        println("------------------------------------------------\n")
        println(text)
        println("------------------------------------------------\n")
    }

    fun summarizeTitle(content: AiVacationContent): AiVacationTitle {
        val systemPrompt = """
        당신은 여행 일정을 한눈에 파악할 수 있는 전문가입니다.
        아래 여행 일정을 최대 15글자로 요약해 주세요.
        장소/테마/특징을 간결하게 써주세요. 불필요한 설명, 감탄사, 접두사 빼고 핵심만!
    """.trimIndent()
        val userPrompt = content.content

        val resp = chatClient.prompt()
            .system(systemPrompt)
            .user(userPrompt)
            .call()
        val summary = resp.content() ?: error("요약 실패")
        // 혹시 15글자 넘는 경우 substring(0, 15) 등 처리
        val response = if (summary.length > 15) summary.take(15) else summary
        return AiVacationTitle(response)
    }


}
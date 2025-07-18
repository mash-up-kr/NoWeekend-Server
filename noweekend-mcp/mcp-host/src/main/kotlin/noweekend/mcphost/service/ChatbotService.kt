package noweekend.mcphost.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import noweekend.mcphost.controller.Prompt
import noweekend.mcphost.controller.request.AiGenerateVacationRequest
import noweekend.mcphost.controller.request.AiGenerateVacationResponse
import noweekend.mcphost.controller.request.SandwichRequest
import noweekend.mcphost.controller.request.Tag
import noweekend.mcphost.controller.request.TagRequest
import noweekend.mcphost.controller.request.WeatherRequest
import noweekend.mcphost.controller.response.BridgeVacationPeriod
import noweekend.mcphost.controller.response.WeatherResponse
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.retry.NonTransientAiException
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientResponseException
import java.time.LocalDate
import java.time.ZoneId

data class DateList(val dates: List<String>)

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
            } catch (e: Exception) {
                if (attempt == 4) throw IllegalStateException("태그 추천을 받아올 수 없습니다.")
            }
        }
        throw IllegalStateException("태그 추천을 받아올 수 없습니다.")
    }

    private val chunkSize = 2

    fun generateVacation(request: AiGenerateVacationRequest): AiGenerateVacationResponse {
        // 1) 샌드위치 날짜 계산 (기존 로직)
        val sandwichJson = objectMapper.writeValueAsString(
            mapOf(
                "days" to request.days,
                "birthDate" to request.birthDate.toString(),
                "upcomingHolidays" to request.upcomingHolidays,
            ),
        )
        val sandwichRaw = chatClient.prompt()
            .system(prompt.sandwichDatePrompt(request))
            .user(sandwichJson)
            .call()
            .content() ?: error("샌드위치 휴가 날짜 생성 실패")
        val cleanedSandwichJson = extractJsonObject(sandwichRaw)
        val allDates = objectMapper.readValue<DateList>(cleanedSandwichJson).dates

        val chunkCache = mutableMapOf<Int, String>()
        val usedItems = mutableListOf<String>()

        val itineraryChunks = allDates.chunked(chunkSize).mapIndexed { idx, datesChunk ->
            chunkCache[idx]?.let { return@mapIndexed it }

            val detailPrompt = prompt.detailedPlanPrompt(request, datesChunk, idx * chunkSize, allDates.size, usedItems)
            val userJson = objectMapper.writeValueAsString(
                mapOf(
                    "dates" to datesChunk,
                    "profile" to minimalProfileMap(request),
                    "totalDays" to allDates.size,
                ),
            )

            while (true) {
                try {
                    val resp = chatClient.prompt()
                        .system(detailPrompt)
                        .user(userJson)
                        .call()
                    val content = resp.content() ?: error("Empty chunk")
                    usedItems += extractUsedItems(content)
                    chunkCache[idx] = content
                    return@mapIndexed content
                } catch (e: NonTransientAiException) {
                    val cause = e.cause
                    val status = (cause as? RestClientResponseException)?.statusCode?.value()
                    val headers = (cause as? RestClientResponseException)?.responseHeaders

                    if (status == 429) {
                        val retryAfterSec = headers
                            ?.getFirst("retry-after")
                            ?.toLongOrNull()
                            ?: 180L

                        logger.warn("429 rate limit hit, waiting $retryAfterSec seconds before retry")
                        Thread.sleep(retryAfterSec * 1000)
                        continue
                    }
                    throw e
                }
            }
        }

        val planRaw = itineraryChunks.joinToString("\n\n")

        val summary = chatClient.prompt()
            .system(
                """
            You are an expert at creating concise and catchy Korean titles for vacation plans.
            Summarize core concept in 15 characters max, Korean only.
                """.trimIndent(),
            )
            .user(
                """
            아래는 사용자 일정입니다:
            $planRaw

            핵심 키워드 중심으로 15자 이내 제목 하나 만들어 주세요.
                """.trimIndent(),
            )
            .call()
            .content() ?: error("제목 요약 실패")

        return AiGenerateVacationResponse(title = summary, content = planRaw)
    }

    private fun minimalProfileMap(req: AiGenerateVacationRequest) = mapOf(
        "days" to req.days,
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

    private fun extractJsonObject(raw: String): String {
        val start = raw.indexOfFirst { it == '{' }
        val end = raw.lastIndexOf('}')
        if (start == -1 || end == -1 || end <= start) {
            error("응답에서 유효한 JSON을 찾을 수 없습니다: $raw")
        }
        return raw.substring(start, end + 1)
    }

    fun getSandwich(request: SandwichRequest): List<BridgeVacationPeriod> {
        request.holidays.forEach { holiday -> println(holiday) }
        request.weekends.forEach { weekend -> println(weekend) }

        val systemPrompt = """
Given the input data below, find all possible "bridge vacation" periods for the rest of the year.

INPUT:
- Public holidays: [${request.holidays.joinToString(", ")}]
- Weekends: [${request.weekends.joinToString(", ")}]

RULES:
1. A "bridge vacation" is a period that connects public holidays and weekends, allowing for up to 1 or 2 weekdays ("gaps") between them, if those weekdays can be replaced with annual leave.
2. If more than 2 consecutive weekdays (gaps) occur, **end the current vacation block before these weekdays begin**, and start a new block from the next holiday or weekend.
3. If after using up the allowed gaps (1 or 2 consecutive weekdays), another holiday or weekend immediately follows, **continue the same block**.
4. If there are 3 or more consecutive weekdays (not holidays or weekends), **break the block** and start a new vacation block after the next holiday/weekend.
5. For each bridge vacation block, output ONLY:
   - "startDate": yyyy-MM-dd (first date of the period)
   - "endDate": yyyy-MM-dd (last date of the period)
6. Only output blocks that are at least 3 days long (inclusive).
7. DO NOT return overlapping or duplicate blocks.
8. Output MUST be a valid JSON array of objects, each with "startDate" and "endDate". 
   - No totalDays, no extra fields.
   - NO markdown, code block, explanation, or any other text—**JSON array ONLY**.
9. If the output includes anything other than the JSON array, it is INVALID.

EXAMPLE (must follow this format exactly):

[
  { "startDate": "2025-10-02", "endDate": "2025-10-09" },
  { "startDate": "2025-12-24", "endDate": "2025-12-28" }
]

***Return ONLY a JSON array as above. No explanation, markdown, or extra text.***
        """.trimIndent()

        val objectMapper = jacksonObjectMapper().findAndRegisterModules()

        var lastException: Throwable? = null
        repeat(30) { attempt ->
            try {
                val rawResponse = chatClient.prompt()
                    .system(systemPrompt)
                    .user("It's Order")
                    .call()
                    .content() ?: throw IllegalStateException("No response from MCP host")

                println("rawResponse = $rawResponse")

                var cleaned = rawResponse.trim()
                if (cleaned.startsWith("```json")) cleaned = cleaned.removePrefix("```json").trim()
                if (cleaned.startsWith("```")) cleaned = cleaned.removePrefix("```").trim()
                if (cleaned.endsWith("```")) cleaned = cleaned.removeSuffix("```").trim()

                val arrStart = cleaned.indexOfFirst { it == '[' }
                val arrEnd = cleaned.lastIndexOf(']')
                if (arrStart != -1 && arrEnd != -1 && arrEnd > arrStart) {
                    cleaned = cleaned.substring(arrStart, arrEnd + 1)
                }

                println("cleaned = $cleaned")

                // 바로 파싱 (실패시 예외 발생)
                return objectMapper.readValue(cleaned)
            } catch (e: Throwable) {
                lastException = e
                println("getSandwich retry ${attempt + 1}/30: ${e.message}")
            }
        }
        throw IllegalStateException("Failed to get valid bridge vacation periods after 30 attempts", lastException)
    }
}

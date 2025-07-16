package noweekend.mcphost.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.UserMessage
import noweekend.mcphost.controller.request.AiGenerateVacationRequest
import noweekend.mcphost.controller.request.AiGenerateVacationResponse
import noweekend.mcphost.controller.request.SandwichRequest
import noweekend.mcphost.controller.request.Tag
import noweekend.mcphost.controller.request.TagRequest
import noweekend.mcphost.controller.request.WeatherRequest
import noweekend.mcphost.controller.response.SandwichResult
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
                    .system(prompt.WEATHER_PROMPT)
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
                    .system(prompt.TAG_SYSTEM_PROMPT)
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
                .system(prompt.ONLY_NEW_TAG_PROMPT)
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

    fun getSandwich(request: SandwichRequest): SandwichResult {
        request.holidays.forEach { holiday -> println(holiday) }
        request.weekends.forEach { weekend -> println(weekend) }


        var remainingAnnualLeaveDays: Int = request.remainingAnnualLeave
        if (remainingAnnualLeaveDays > 4) {
            remainingAnnualLeaveDays = 4
        }
        val systemPrompt = """
Given the following input data:
- All remaining public holidays this year: [${request.holidays.joinToString(", ") { it.toString() }}]
- All remaining weekends this year: [${request.weekends.joinToString(", ") { it.toString() }}]
- Your birthday: ${request.birthDay}
- The number of annual leave days you can use: ${remainingAnnualLeaveDays}

**Rules:**
1. Output MUST be a single, valid JSON object. ABSOLUTELY NO natural language, explanation, markdown, or code block (no backticks, no text before or after the JSON).
2. Object keys MUST be: "useAnnualLeaveN" (N = 4, 3, ..., 1), ONLY for N that results in a vacation longer than 2 days.
3. Each value is an array of vacation objects for that leave count, each with:
   - "startDate": LocalDate (yyyy-MM-dd)
   - "endDate": LocalDate (yyyy-MM-dd)
   - "totalDays": Int (number of consecutive days off)
   - "usedAnnualLeaveDates": Array of LocalDate (yyyy-MM-dd) — **this MUST be a list of only NORMAL weekdays (not holidays or weekends) when annual leave is actually used**.
4. **CRITICAL**: Every date in "usedAnnualLeaveDates" MUST NOT be:
   - a public holiday (see input list above)
   - a weekend (see input list above)
   - If any "usedAnnualLeaveDates" entry matches a holiday or weekend, the result is INVALID. Repeat: **No annual leave on holidays or weekends, ever.**
5. "usedAnnualLeaveDates" array length MUST be exactly N (the leave count in the key). Only include dates that are eligible for annual leave (weekdays, not in holidays or weekends).
6. DO NOT include duplicate or overlapping vacation periods across any array.
7. DO NOT include keys for N where no vacation longer than 2 days is possible.
8. Output must be JSON only. No text, no explanation, no markdown.

**Output example (ONLY THIS FORMAT, NO OTHERS):**
{
  "useAnnualLeave4": [
    {
      "startDate": "2025-10-01",
      "endDate": "2025-10-13",
      "totalDays": 13,
      "usedAnnualLeaveDates": ["2025-10-01", "2025-10-02", "2025-10-10", "2025-10-13"]
      // Each date above is a weekday, not a holiday or weekend
    }
  ],
  "useAnnualLeave3": [
    {
      "startDate": "2025-10-02",
      "endDate": "2025-10-13",
      "totalDays": 12,
      "usedAnnualLeaveDates": ["2025-10-02", "2025-10-10", "2025-10-13"]
    }
  ]
}

**REPEAT: Return ONLY the JSON object above. NEVER include any explanation, markdown, or non-JSON text. If any annual leave is placed on a holiday or weekend, the output is INVALID.**
""".trimIndent()



        val objectMapper = jacksonObjectMapper().findAndRegisterModules()

        val sandwichResult: SandwichResult = retryJsonParse(
            times = 10,
            block = {
                chatClient.prompt()
                    .system(systemPrompt)
                    .user("It's Order")
                    .call()
                    .content() ?: throw IllegalStateException("No response from MCP host")
            },
            parse = { fixedContent -> objectMapper.readValue(fixedContent, SandwichResult::class.java) }
        )

        sandwichResult.useAnnualLeave4.forEach { println(it) }
        sandwichResult.useAnnualLeave3.forEach { println(it) }
        sandwichResult.useAnnualLeave2.forEach { println(it) }
        sandwichResult.useAnnualLeave1.forEach { println(it) }

        return sandwichResult
    }

}

fun <T> retryJsonParse(times: Int = 3, block: () -> String, parse: (String) -> T): T {
    var last: Throwable? = null
    repeat(times) {
        try {
            val content = block()
            // 아래는 JSON만 추출하는 부분(코드 재활용)
            var fixedContent = content.trim()
            if (fixedContent.startsWith("```json")) {
                fixedContent = fixedContent.removePrefix("```json").trim()
            }
            if (fixedContent.startsWith("```")) {
                fixedContent = fixedContent.removePrefix("```").trim()
            }
            if (fixedContent.endsWith("```")) {
                fixedContent = fixedContent.removeSuffix("```").trim()
            }
            return parse(fixedContent)
        } catch (e: Throwable) {
            last = e
        }
    }
    throw last ?: IllegalStateException("Unknown error in retryJsonParse")
}

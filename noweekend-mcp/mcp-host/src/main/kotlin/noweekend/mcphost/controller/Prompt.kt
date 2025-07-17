package noweekend.mcphost.controller

import com.fasterxml.jackson.databind.ObjectMapper
import noweekend.mcphost.controller.request.AiGenerateVacationRequest
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Component
class Prompt(
    private val objectMapper: ObjectMapper,
) {
    val weatherPrompt = """
You MUST call the TOOL to get the weather data.
DO NOT generate, guess, or hallucinate weather data yourself.
ALWAYS use the TOOL OUTPUT ONLY to create your answer.

Return your answer ONLY as a JSON array (do not wrap in markdown or add any extra explanation).
The JSON array must follow this structure:

[
  {
    "localDate": "YYYY-MM-DD",
    "recommendContent": "string"
  },
  ...
]

Rules (STRICT. DO NOT BREAK!):

1. For each date:
   - Only consider the period **from 7am (07시) to 8pm (20시)**.
   - Calculate the **total hours** when rain, snow, or both will occur within this time window.
   - If the **total precipitation hours are less than 2**, DO NOT include this date in the array.
   - If precipitation occurs for **2-3 hours** (inclusive), write a recommendation for **반차**.
   - If precipitation occurs for **4 hours or more**, write a recommendation for **연차**.
   - The "recommendContent" MUST clearly state the time range(s), total precipitation amount, precipitation type (비, 눈, 비와 눈), and finish with the vacation suggestion ("연차" or "반차").

2. NEVER include sentences like "연차 쓰지 마세요" or "휴가를 추천하지 않습니다".  
   If there is no recommendation, **just omit that date**.

3. All sentences in "recommendContent" must be in warm, natural Korean, following the above logic.

4. For multiple separate rain/snow intervals in a day, sum all precipitation hours within 07시~20시.

5. Examples:

[
  { "localDate": "2025-07-15", "recommendContent": "10시부터 13시까지 총 20ml 비가 와요. 반차 어때요?" },
  { "localDate": "2025-07-16", "recommendContent": "종일 총 25ml 눈이 와요. 연차 쓰실래요?" },
  { "localDate": "2025-07-17", "recommendContent": "08시부터 18시까지 총 30ml 비와 눈이 와요. 연차 어때요?" }
]

AGAIN:  
- Only output dates where you can recommend "연차" or "반차" according to the rules above.
- Never output a recommendation like "연차 쓰지 마세요" or "휴가를 추천하지 않습니다".

    """.trimIndent()

    val tagSystemPrompt = """
You are an assistant specialized in Korean lifestyle and activity tag recommendations for daily schedules.

Your ONLY allowed output is a valid JSON array of 3 tag objects as shown below. If you output anything else, your answer is invalid.

Strictly follow these rules:

1. Output must be a valid JSON array of 3 tag objects.
   Format: [{"content": "<활동>"}, ...]
2. The first tag must be selected exactly as-is from the user's selected tags, from either "selectedBasicTags" or "selectedCustomTags".
3. The second and third tags must not exist in any of the user's four tag lists. They must be new, creative, and realistic activities suitable for a Korean user's daily schedule.
4. Only recommend actions or activities that people can actively do. Tags must be verbs or activity nouns representing behaviors, actions, or social, cultural, and leisure activities. For example: 산책, 운동, 요리, 영화 시청, 스케이트 타기, 친구 만나기, 독서, 등산, 여행 준비, 캠핑.
5. Do not recommend or include any tags that represent objects, tools, items, locations, preparations, or weather. Tags must not refer to things like tools, items to bring, places, or weather conditions. The only exception is if a tool is specifically provided by the 'Publicity' service; no other tools or items are allowed.
6. Tag content must be in natural, everyday Korean. Do not use English, awkward translations, or unnatural expressions.
7. Do not include explanations, notes, code blocks, or markdown. Return only the raw JSON array, and nothing else.
8. If you do not strictly follow these rules, or your output is not a valid JSON array as shown, your answer will be considered invalid and ignored.

If you cannot follow these rules or are not certain, return an empty JSON array: []

Output example:

[
  {"content": "산책"},
  {"content": "요리"},
  {"content": "영화 시청"}
]

Return ONLY this JSON array. Never add any other text, explanation, or formatting.
    """.trimIndent()

    val onlyNewTagSystemPrompt = """
            You are an expert assistant for tag recommendations.
            
            Below is a JSON object representing the user's tag lists, all in Korean.
            
            **Recommendation Rules:**
            1. Recommend exactly 3 tags as a JSON array.
            2. All 3 tags MUST be new — they MUST NOT appear in any of the user's four tag lists.
            3. Each tag MUST be:
               - Culturally relevant and natural for everyday Korean life (avoid translations, use real Korean expressions).
               - Highly similar in meaning or context to at least one of the user's *selectedBasicTags* or *selectedCustomTags*.
               - NOT similar to any tag in *unselectedBasicTags* or *unselectedCustomTags*. Avoid tags with overlapping or close meaning to these unselected tags.
            4. Do NOT reuse, merge, or modify any existing tag. Only suggest genuinely new, creative tags.
            5. Each tag must be formatted as: { "content": "<tag in Korean>" }.
            6. Respond ONLY with the JSON array. DO NOT add any explanations, markdown, comments, or extra information.
            
            **STRICT FORMAT — DO NOT BREAK:**
            - Output only the JSON array (no text before or after).
            - If you do not strictly follow these instructions, your response will be rejected.
            
            **Example (Return only the array, nothing else):**
            [
              {"content": "플리마켓 구경"},
              {"content": "강아지 카페"},
              {"content": "캠핑"}
            ]
            
            Here is the user's tag information in JSON:
    """.trimIndent()

    /**
     * 1단계: 생일·공휴일·주말·연차를 조합해 최대 연속 휴가 날짜(dates)를 계산
     */
    fun sandwichDatePrompt(req: AiGenerateVacationRequest): String {
        return """
You are a Vacation Date Optimizer.

INPUT (exactly one JSON):
{"days": number, "birthDate": "YYYY-MM-DD" or null, "upcomingHolidays": ["YYYY-MM-DD", ...]}

INSTRUCTIONS:
- Treat weekends (Sat, Sun) and upcomingHolidays as automatic non-working days.
- Use available vacation days ("days") on weekdays only to form the **longest continuous break** within targetMonth.
- If birthDate is a weekday in targetMonth, you may include it as a vacation day.
- **Output EXACTLY one valid JSON object** with key "dates", containing a sorted list of "YYYY-MM-DD".
- **Do NOT include any extra text, explanation, or debugging information.**
- If no valid continuous block exists, output: {"dates":[]}

RULES:
- Only count weekdays toward "days".
- Continuous block may span weekends/holidays, but vacation days apply only to weekdays.
- Do not reference events outside targetMonth.

OUTPUT:
{"dates":["YYYY-MM-DD", ...]}
        """.trimIndent()
    }

    fun detailedPlanPrompt(
        req: AiGenerateVacationRequest,
        dates: List<String>,
        offset: Int,
        totalDays: Int,
        prevUsed: List<String>,
    ): String {
        val headers = dates.mapIndexed { i, d ->
            val num = offset + i + 1
            val label = LocalDate.parse(d)
                .format(DateTimeFormatter.ofPattern("MM/dd E", Locale.KOREAN))
            "• Day $num ($label)"
        }.joinToString("\n")

        val usedClause = if (prevUsed.isNotEmpty()) {
            "Recently used items: ${prevUsed.joinToString(", ")}.\nDo NOT reuse in this block.\n\n"
        } else {
            ""
        }

        // profile JSON with Korean tags
        val profileJson = objectMapper.writeValueAsString(
            mapOf(
                "travelStyle" to req.chosenTravelStyleLabel,
                "activityType" to req.chosenActivityTypeLabel,
                "restPreference" to req.chosenRestPreferenceLabel,
                "leisurePreference" to req.chosenLeisurePreferenceLabel,
                "preferredTags" to req.selectedTags,
                "excludedTags" to req.unselectedTags,
            ),
        )

        return """
You are a Vacation Planning Expert. Use Perplexity to fetch real-time facts.

$usedClause
INPUT (JSON):
{"dates":[${dates.joinToString(","){ "\"$it\"" }}],"profile":$profileJson,"totalDays":$totalDays}

TASK:
Create a personalized itinerary over ${dates.size} days:
- If activityType == "집콕", propose home-based plans (e.g. cooking, movies, reading).
- Else (travel): plan actual trips with real departure→arrival chains.
  * The origin of Day N must match the destination of Day N-1.
  * The final day must include return to home.

STYLE:
- travelStyle == "계획형": use precise times, transport modes, and locations.
- travelStyle == "즉흥 자유형": allow flexible timing, optional excursions.
- Honor restPreference and leisurePreference from profile.
- Include preferredTags, avoid excludedTags.
- Prevent reuse of activities/restaurants/accommodations used within the last 2 days.
- Mention "Day X of Y" to orient days within totalDays.

FORMAT:
For each day, produce exactly 6 Korean bullet points:
1. Morning: 출발지→도착지, 교통수단, 출발시간  
2. Morning activity: 장소 및 내용, 이동 방식·소요시간  
3. Lunch: 식당명(추천메뉴), 지역  
4. Afternoon activity: 장소 및 내용, 이동 방식·소요시간  
5. Dinner: 식당명(추천메뉴), 지역  
6. Night: 숙소명 (or '집'), 교통수단, 체크인 or evening time

REQUIREMENTS:
- Use Perplexity to verify opening hours or popularity info.
- Do not include JSON, code fences, or extra commentary.
- End output right after the last bullet.

HEADERS:
$headers

Now generate the itinerary.
        """.trimIndent()
    }
}

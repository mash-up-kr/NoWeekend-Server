package noweekend.mcphost.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import noweekend.mcphost.controller.WeatherRequest
import noweekend.mcphost.controller.WeatherResponse
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
        val prompt = """
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

Rules:
- For each date, generate one "recommendContent" (Korean, very friendly and natural).
- "recommendContent" MUST clearly mention the weather situation (e.g., rain, snow, or mixed), AND be polite, soft, and friendly (not formal or stiff).
- "recommendContent" MUST be within 15 Korean characters. If it's longer, shorten it, but always include the weather info first.
- Use warm, casual, and kind expressions (e.g., "오후에 비가 온대요, 연차 어때요?", "눈 온다니 연차 써볼래요?", "종일 비예요, 오늘은 쉬어요!") 
- Do NOT use phrases like "권장합니다", "추천합니다".
- Each object must have "localDate" (YYYY-MM-DD) and "recommendContent" (max 15 Korean chars, weather included).
- ONLY output the JSON array, with no other explanations, markdown, or extra text.

Follow these weather-based rules for "recommendContent":
- If rain or snow is predicted for 1-4 consecutive hours in the morning: include "오전" and the weather (e.g., "오전에 비가 와요, 연차 어때요?")
- If 1-4 hours in the afternoon: "오후에 눈 온대요, 연차 어때요?"
- If 4-6 hours: summarize (e.g., "비 많이 와요, 쉬는 건 어때요?")
- If 6+ hours or "RAIN_AND_SNOW": "종일 비예요, 오늘은 쉬어요!"
- Always mention the weather type (rain, snow, or mixed) at the beginning of "recommendContent".
- If no tool output for a day, SKIP.

AGAIN: Respond ONLY with the above JSON array, nothing else.
        """.trimIndent()

        val baseDate = LocalDate.now(ZoneId.of("Asia/Seoul")).toString()
        val userMsg = """
latitude: ${request.latitude}
longitude: ${request.longitude}
baseDate: $baseDate
        """.trimIndent()

        // 최대 2회까지 재시도
        repeat(2) { attempt ->
            val jsonString = chatClient.prompt()
                .system(prompt)
                .user(userMsg)
                .call()
                .content()
            try {
                if (jsonString != null) {
                    return objectMapper.readValue(jsonString)
                }
            } catch (e: Exception) {
                // 첫 실패면 재시도, 두번째 실패면 아래 throw
                if (attempt == 1) {
                    throw IllegalStateException("현재 날씨 정보를 받아올 수 없습니다.")
                }
            }
        }
        throw IllegalStateException("현재 날씨 정보를 받아올 수 없습니다.")
    }
}

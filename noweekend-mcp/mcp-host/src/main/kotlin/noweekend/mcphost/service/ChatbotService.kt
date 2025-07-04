package noweekend.mcphost.service

import noweekend.mcphost.controller.WeatherRequest
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId

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

    fun weatherRecommendation(request: WeatherRequest): String {
        val prompt = """
YOU MUST CALL THE TOOL to get the weather data.
Do NOT generate, guess, or hallucinate weather data yourself.
ALWAYS use the TOOL OUTPUT ONLY to create your answer.

Today's date is the current date in KOREAN STANDARD TIME (KST).
You are REQUIRED to use weather data from the tool for up to 3 days starting from today (in KST).
ONLY provide recommendations for those dates.

The tool returns grouped, consecutive periods of rain, snow, or mixed (rain & snow) within work hours (07:00~20:00).

Example TOOL OUTPUT (dates and times are just samples, do not use them literally):
[
  {
    "date": "nnnn-nn-nn",
    "rainSnowInfos": [
      { "type": "RAIN" },
      { "type": "SNOW" },
      { "type": "RAIN_AND_SNOW" }
    ]
  }
]

- "type": "RAIN" (rain), "SNOW" (snow), "RAIN_AND_SNOW" (mixed), "POSSIBLE_RAIN" (possible rain, 60%+ chance)

Based STRICTLY on the provided periods for each date, generate annual leave or half-day leave recommendations in Korean:
- If rain/snow is predicted for 1-4 consecutive hours in the morning: "Recommend taking morning half-day leave."
- If rain/snow is predicted for 1-4 consecutive hours in the afternoon: "Recommend taking afternoon half-day leave."
- If 4-6 consecutive hours: "Recommend taking a full day off."
- If 6+ consecutive hours: "Recommend taking a full day off due to all-day precipitation."
- If any period is "RAIN_AND_SNOW": Always "Recommend taking a full day off."
- For each day, return a single recommendation sentence in Korean.

IF THERE IS NO TOOL OUTPUT FOR A DAY, SKIP THAT DAY.

DO NOT return anything except the final Korean recommendation sentences for each valid date.
        """.trimIndent()

        val baseDate = LocalDate.now(ZoneId.of("Asia/Seoul")).toString()
        val userMsg = """
latitude: ${request.latitude}
longitude: ${request.longitude}
baseDate: $baseDate
        """.trimIndent()

        return chatClient.prompt()
            .system(prompt)
            .user(userMsg)
            .call()
            .content()
            ?: throw IllegalStateException("Weather recommendation response is null")
    }
}

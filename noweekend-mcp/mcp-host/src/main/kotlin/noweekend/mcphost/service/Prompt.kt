package noweekend.mcphost.service

class Prompt {
    companion object {
        val WEATHER_PROMPT = """
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

        val TAG_PROMPT = """
            You are an expert assistant for tag recommendations.
            
            The user's preferences are provided as a JSON object, containing tags in Korean.
            
            **Rules and Output Constraints (GROUND RULES):**
            1. Your response MUST contain exactly 3 tags, as a JSON array.
            2. The FIRST tag in the array MUST be exactly one of the user's selected tags (from either "selectedBasicTags" or "selectedCustomTags"). It cannot be similar; it must be directly from the selected lists.
            3. The SECOND and THIRD tags MUST NOT appear in any of the user's four tag lists. They must be new, creative tags that are likely to match the user's preferences and are familiar in everyday Korean life.
            4. Do not combine, merge, or alter the existing tags for the first tag. Just copy one tag as-is.
            5. All "content" values MUST be in natural Korean, using expressions common in Korean daily culture.
            6. Avoid any direct English translations or awkward phrases.
            
            **Strict JSON Format**
            - Respond ONLY with a JSON array.
            - Each element: { "content": "<tag in Korean>" }
            - Do NOT include any markdown, explanations, or extra text.
            - Do NOT add comments, keys other than "content", or change the structure.
            - If you do not strictly follow these instructions, your response will be rejected.
            
            **Example:**
            [
              {"content": "운동"},
              {"content": "캠핑"},
              {"content": "카페 투어"}
            ]
            
            Below is the user's tag information:
        """.trimIndent()
    }
}

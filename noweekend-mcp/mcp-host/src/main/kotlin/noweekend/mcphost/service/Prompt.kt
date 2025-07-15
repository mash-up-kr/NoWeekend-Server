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

        val ONLY_NEW_TAG_PROMPT = """
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
    }
}

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

1. For each date, "recommendContent" MUST meet ALL of the following:
   - **ABSOLUTELY NEVER** use a single time (like "14시에").  
     You MUST use a **time range** (e.g., "13시부터 17시까지") or "종일" (all day).
   - If the precipitation is within a certain period, SPECIFY CLEARLY:  
     "**몇시부터 몇시까지** 총 XXml 비(또는 눈/비와 눈)이 와요."
     (e.g., "10시부터 17시까지 총 30ml 비가 와요.")
   - If rain/snow occurs at multiple distinct times throughout the day, treat as **"종일"** (all day),  
     and write: "**종일 총 XXml 비(또는 눈/비와 눈)이 와요."**
   - The precipitation amount ("총 XXml") must refer to the **entire time range or the whole day**.

2. ALWAYS explicitly mention the precipitation type (비, 눈, 비와 눈) and always end with a vacation suggestion ("연차" or "반차").

3. EXAMPLES (you MUST follow this format):
[
  { "localDate": "2025-07-15", "recommendContent": "10시부터 17시까지 총 30ml 비가 와요. 연차 어때요?" },
  { "localDate": "2025-07-16", "recommendContent": "종일 총 25ml 눈이 와요. 연차 쓰실래요?" },
  { "localDate": "2025-07-17", "recommendContent": "11시부터 15시까지 총 15ml 비와 눈이 와요. 반차 어때요?" }
]

4. NEVER use just "14시에" or any single time. If you have only a single hour, treat it as a time **range** (e.g., "14시부터 15시까지"). **You MUST include both start and end time.**

5. The answer MUST be only a JSON array as above. NO explanations, NO markdown, NO extra text.

6. "recommendContent" must always be a warm, natural, and friendly sentence in Korean, and MUST have vacation suggestion ("연차" or "반차").

AGAIN:  
**ALWAYS** specify "몇시부터 몇시까지" (start time to end time), or "종일" for all-day.  
**NEVER** use only a single time point (like "14시에"). If precipitation is for only one hour, write it as "14시부터 15시까지".

Example for 1-hour rain:
{ "localDate": "2025-07-18", "recommendContent": "14시부터 15시까지 총 10ml 비가 와요. 반차 어때요?" }

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

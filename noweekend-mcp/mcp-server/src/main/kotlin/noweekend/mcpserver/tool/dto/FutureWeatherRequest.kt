package noweekend.mcpserver.tool.dto

import org.springframework.ai.tool.annotation.ToolParam

/**
 * LLM 도구용 입력 파라미터
 */
data class FutureWeatherRequest(
    @field:ToolParam(description = "위도 (예: 37.57)", required = true)
    val latitude: Double,

    @field:ToolParam(description = "경도 (예: 126.98)", required = true)
    val longitude: Double,

    @field:ToolParam(description = "조회 시작 날짜 (YYYY-MM-DD)", required = true)
    val startDate: String,

    @field:ToolParam(description = "조회 종료 날짜 (YYYY-MM-DD)", required = true)
    val endDate: String,
)

package noweekend.mcpserver.tool.dto

/**
 * 일별 기후 데이터
 *
 * @param date ISO-8601 형식 날짜 (YYYY-MM-DD)
 * @param meanTemp 평균 기온 (°C)
 * @param precipitation 일일 강수합 (mm)
 */
data class DailyClimateData(
    val date: String,
    val meanTemp: Double,
    val precipitation: Double,
)

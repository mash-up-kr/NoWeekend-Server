package noweekend.mcpserver.tool.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Open-Meteo Climate API 응답을 매핑하는 DTO
 */
data class ClimateResponse(
    val latitude: Double,
    val longitude: Double,
    @JsonProperty("generationtime_ms")
    val generationTimeMs: Double,
    @JsonProperty("utc_offset_seconds")
    val utcOffsetSeconds: Int,
    val timezone: String,
    @JsonProperty("timezone_abbreviation")
    val timezoneAbbreviation: String,
    val elevation: Double,
    @JsonProperty("daily_units")
    val dailyUnits: DailyUnits,
    val daily: DailyData,
) {
    data class DailyUnits(
        val time: String,
        @JsonProperty("temperature_2m_mean")
        val temperature2mMean: String,
        @JsonProperty("precipitation_sum")
        val precipitationSum: String,
    )

    data class DailyData(
        val time: List<String>,
        @JsonProperty("temperature_2m_mean")
        val temperature2mMean: List<Double>,
        @JsonProperty("precipitation_sum")
        val precipitationSum: List<Double>,
    )
}

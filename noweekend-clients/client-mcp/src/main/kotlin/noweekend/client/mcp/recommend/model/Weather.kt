package noweekend.client.mcp.recommend.model

import java.time.LocalDate

data class WeatherRequest(
    val latitude: Double,
    val longitude: Double,
)

data class WeatherResponse(
    val localDate: LocalDate,
    val recommendContent: String,
)

package noweekend.mcphost.controller

import java.time.LocalDate

data class WeatherResponse(
    val localDate: LocalDate,
    val recommendContent: String,
)

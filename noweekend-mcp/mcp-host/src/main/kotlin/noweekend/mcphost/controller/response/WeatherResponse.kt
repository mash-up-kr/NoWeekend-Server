package noweekend.mcphost.controller.response

import java.time.LocalDate

data class WeatherResponse(
    val localDate: LocalDate,
    val recommendContent: String,
)

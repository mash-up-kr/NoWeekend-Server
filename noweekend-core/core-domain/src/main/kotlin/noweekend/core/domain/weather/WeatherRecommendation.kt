package noweekend.core.domain.weather

import java.time.LocalDate

data class WeatherRecommendation(
    val localDate: LocalDate,
    val recommendContent: String,
)

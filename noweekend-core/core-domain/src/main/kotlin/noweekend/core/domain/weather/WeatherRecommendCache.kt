package noweekend.core.domain.weather

import noweekend.core.domain.util.IdGenerator
import java.time.LocalDate
import java.time.LocalDateTime

data class WeatherRecommendCache(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val searchDate: LocalDate,
    val weatherResponses: List<WeatherRecommendation>,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun register(
            latitude: Double,
            longitude: Double,
            searchDate: LocalDate,
            weatherResponses: List<WeatherRecommendation>,
        ): WeatherRecommendCache {
            return WeatherRecommendCache(
                id = IdGenerator.generate(),
                latitude = latitude,
                longitude = longitude,
                searchDate = searchDate,
                weatherResponses = weatherResponses,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        }
    }
}

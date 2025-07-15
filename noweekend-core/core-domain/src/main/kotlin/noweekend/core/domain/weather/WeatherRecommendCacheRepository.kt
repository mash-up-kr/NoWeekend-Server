package noweekend.core.domain.weather

import java.time.LocalDate

interface WeatherRecommendCacheRepository {
    fun findCacheByLocationAndDate(
        latitude: Double,
        longitude: Double,
        searchDate: LocalDate,
    ): WeatherRecommendCache?

    fun register(weatherRecommendCache: WeatherRecommendCache)
}

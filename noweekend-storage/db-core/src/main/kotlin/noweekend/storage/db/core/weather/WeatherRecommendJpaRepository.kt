package noweekend.storage.db.core.weather

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface WeatherRecommendJpaRepository : JpaRepository<WeatherRecommendCacheEntity, String> {
    fun findByLatitudeAndLongitudeAndSearchDate(
        latitude: Double,
        longitude: Double,
        searchDate: LocalDate,
    ): WeatherRecommendCacheEntity?
}

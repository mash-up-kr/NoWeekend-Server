package noweekend.storage.db.core.weather

import noweekend.core.domain.weather.WeatherRecommendCache
import noweekend.core.domain.weather.WeatherRecommendCacheRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class WeatherRecommendCoreRepository(
    private val jpaRepository: WeatherRecommendJpaRepository,
    private val converter: WeatherRecommendCacheConverter,
) : WeatherRecommendCacheRepository {
    override fun findCacheByLocationAndDate(
        latitude: Double,
        longitude: Double,
        searchDate: LocalDate,
    ): WeatherRecommendCache? {
        return jpaRepository.findByLatitudeAndLongitudeAndSearchDate(
            latitude = latitude,
            longitude = longitude,
            searchDate = searchDate,
        )?.let { converter.entityToDomain(it) }
    }

    override fun register(
        weatherRecommendCache: WeatherRecommendCache,
    ) {
        jpaRepository.save(converter.domainToEntity(weatherRecommendCache))
    }
}

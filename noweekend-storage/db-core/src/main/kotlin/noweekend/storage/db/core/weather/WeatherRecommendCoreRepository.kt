package noweekend.storage.db.core.weather

import noweekend.core.domain.weather.WeatherRecommendCache
import noweekend.core.domain.weather.WeatherRecommendCacheRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class WeatherRecommendCoreRepository(
    private val jpaRepository: WeatherRecommendJpaRepository,
    private val converter: WeatherRecommendCacheConverter,
) : WeatherRecommendCacheRepository {

    private val log = LoggerFactory.getLogger(this::class.java)

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

    override fun register(weatherRecommendCache: WeatherRecommendCache) {
        try {
            jpaRepository.save(converter.domainToEntity(weatherRecommendCache))
        } catch (e: DataIntegrityViolationException) {
            log.debug("Cache already exists for location and date", e)
        }
    }
}

package noweekend.core.domain.weather

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
@Transactional(readOnly = true)
class WeatherReader(
    private val repository: WeatherRecommendCacheRepository,
) {
    fun findCacheByLocationAndDate(
        latitude: Double,
        longitude: Double,
        searchDate: LocalDate,
    ): WeatherRecommendCache? {
        return repository.findCacheByLocationAndDate(latitude, longitude, searchDate)
    }
}

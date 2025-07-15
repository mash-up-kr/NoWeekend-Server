package noweekend.core.domain.weather

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class WeatherWriter(
    private val repository: WeatherRecommendCacheRepository,
) {
    fun register(weatherRecommendCache: WeatherRecommendCache) {
        repository.register(weatherRecommendCache)
    }
}

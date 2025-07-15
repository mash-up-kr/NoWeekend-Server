package noweekend.storage.db.core.weather

import com.fasterxml.jackson.databind.ObjectMapper
import noweekend.core.domain.weather.WeatherRecommendCache
import noweekend.core.domain.weather.WeatherRecommendation
import org.springframework.stereotype.Component

@Component
class WeatherRecommendCacheConverter(
    private val objectMapper: ObjectMapper,
) {
    fun entityToDomain(entity: WeatherRecommendCacheEntity): WeatherRecommendCache =
        WeatherRecommendCache(
            id = entity.id,
            latitude = entity.latitude,
            longitude = entity.longitude,
            searchDate = entity.searchDate,
            weatherResponses = objectMapper.readValue(
                entity.recommendJson,
                object : com.fasterxml.jackson.core.type.TypeReference<List<WeatherRecommendation>>() {},
            ),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )

    fun domainToEntity(domain: WeatherRecommendCache): WeatherRecommendCacheEntity =
        WeatherRecommendCacheEntity(
            id = domain.id,
            latitude = domain.latitude,
            longitude = domain.longitude,
            searchDate = domain.searchDate,
            recommendJson = objectMapper.writeValueAsString(domain.weatherResponses),
        )
}

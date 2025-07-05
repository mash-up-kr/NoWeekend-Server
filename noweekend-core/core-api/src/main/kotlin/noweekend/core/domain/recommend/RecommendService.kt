package noweekend.core.domain.recommend

import noweekend.core.api.controller.v1.response.WeatherApiResponse

interface RecommendService {
    fun getWeatherRecommend(userId: String): WeatherApiResponse
}

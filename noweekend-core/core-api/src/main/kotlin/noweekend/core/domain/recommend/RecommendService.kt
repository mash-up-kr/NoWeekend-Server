package noweekend.core.domain.recommend

import noweekend.client.mcp.recommend.model.TagApiResponses
import noweekend.core.api.controller.v1.response.WeatherApiResponse

interface RecommendService {
    fun getWeatherRecommend(userId: String): WeatherApiResponse
    fun getTagRecommend(userId: String): TagApiResponses
}

package noweekend.core.domain.recommend

import noweekend.client.mcp.recommend.model.SandwichResponse
import noweekend.client.mcp.recommend.model.TagApiResponses
import noweekend.core.api.controller.v1.response.WeatherResponse

interface RecommendService {
    fun getWeatherRecommend(userId: String): WeatherResponse
    fun getTagRecommend(userId: String): TagApiResponses
    fun getTagRecommendOnlyNew(userId: String): TagApiResponses
    fun getSandwich(userId: String): SandwichResponse
}

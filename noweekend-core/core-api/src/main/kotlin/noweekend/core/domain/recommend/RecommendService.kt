package noweekend.core.domain.recommend

import noweekend.client.mcp.recommend.model.SandwichResponse
import noweekend.core.api.controller.v1.response.WeatherResponse
import noweekend.core.domain.tag.TagRecommendations

interface RecommendService {
    fun getWeatherRecommend(userId: String): WeatherResponse
    fun getTagRecommend(userId: String): TagRecommendations
    fun getTagRecommendOnlyNew(userId: String): TagRecommendations
    fun getSandwich(userId: String): SandwichResponse
}

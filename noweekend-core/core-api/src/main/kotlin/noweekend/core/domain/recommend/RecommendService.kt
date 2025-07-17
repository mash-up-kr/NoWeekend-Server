package noweekend.core.domain.recommend

import noweekend.client.mcp.recommend.model.SandwichApiResponse
import noweekend.core.api.controller.v1.request.GenerateVacationRequest
import noweekend.core.api.controller.v1.response.AiGenerateVacationApiResponse
import noweekend.core.api.controller.v1.response.WeatherResponse
import noweekend.core.domain.tag.TagRecommendations

interface RecommendService {
    fun getWeatherRecommend(userId: String): WeatherResponse
    fun getTagRecommend(userId: String): TagRecommendations
    fun getTagRecommendOnlyNew(userId: String): TagRecommendations
    fun getSandwich(userId: String): SandwichApiResponse
    fun generateVacation(userId: String, request: GenerateVacationRequest): AiGenerateVacationApiResponse
}

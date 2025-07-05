package noweekend.core.api.controller.v1

import noweekend.core.api.controller.v1.response.WeatherApiResponse
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.domain.recommend.RecommendService
import noweekend.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/recommend")
class RecommendController(
    private val recommendService: RecommendService,
) : RecommendControllerDocs {

    @GetMapping("/weather")
    override fun getWeatherRecommend(
        @CurrentUserId userId: String,
    ): ApiResponse<WeatherApiResponse> {
        return ApiResponse.success(recommendService.getWeatherRecommend(userId))
    }
}

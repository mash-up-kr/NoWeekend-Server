package noweekend.core.api.controller.v1

import noweekend.core.api.controller.v1.docs.RecommendControllerDocs
import noweekend.core.api.controller.v1.request.GenerateVacationRequest
import noweekend.core.api.controller.v1.response.AiVacationApiResponse
import noweekend.core.api.controller.v1.response.SandwichApiResponse
import noweekend.core.api.controller.v1.response.WeatherResponse
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.domain.recommend.RecommendService
import noweekend.core.domain.tag.TagRecommendations
import noweekend.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    ): ApiResponse<WeatherResponse> {
        return ApiResponse.success(recommendService.getWeatherRecommend(userId))
    }

    @GetMapping("/todo/mixed")
    override fun getTagRecommendMixed(
        @CurrentUserId userId: String,
    ): ApiResponse<TagRecommendations> {
        return ApiResponse.success(
            recommendService.getTagRecommend(userId),
        )
    }

    @GetMapping("/todo/new-only")
    override fun getTagRecommendOnlyNew(
        @CurrentUserId userId: String,
    ): ApiResponse<TagRecommendations> {
        return ApiResponse.success(
            recommendService.getTagRecommendOnlyNew(userId),
        )
    }

    @GetMapping("/sandwich")
    override fun getSandwich(): ApiResponse<SandwichApiResponse> {
        val sandwichApiResponse = recommendService.getSandwich()
        return ApiResponse.success(sandwichApiResponse)
    }

    @PostMapping("/vacation")
    override fun generateVacation(
        @CurrentUserId userId: String,
        @RequestBody request: GenerateVacationRequest,
    ): ApiResponse<String> {
        recommendService.generateVacation(userId, request)
        return ApiResponse.success("휴가 생성 요청이 완료되었습니다.")
    }

    @GetMapping("/vacation")
    override fun getVacation(
        @CurrentUserId userId: String,
        @RequestBody request: GenerateVacationRequest,
    ): ApiResponse<AiVacationApiResponse> {
        return ApiResponse.success(
            recommendService.getVacation(userId),
        )
    }
}

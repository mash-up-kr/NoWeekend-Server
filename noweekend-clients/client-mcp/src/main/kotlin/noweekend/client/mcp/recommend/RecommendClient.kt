package noweekend.client.mcp.recommend

import feign.FeignException
import noweekend.client.mcp.recommend.model.SandwichRequest
import noweekend.client.mcp.recommend.model.SandwichResponse
import noweekend.client.mcp.recommend.model.TagRequest
import noweekend.client.mcp.recommend.model.WeatherRequest
import noweekend.core.domain.tag.TagRecommendation
import noweekend.core.domain.tag.TagRecommendations
import noweekend.core.domain.tag.UserTags
import noweekend.core.domain.weather.WeatherRecommendation
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class RecommendClient(
    private val api: RecommendApi,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun getFutureWeather(request: WeatherRequest): List<WeatherRecommendation> {
        try {
            return api.getFutureWeather(request)
        } catch (e: FeignException) {
            log.error("[getFutureWeather] FeignException occurred. status=${e.status()}, msg=${e.message}", e)
            throw e
        } catch (e: Exception) {
            log.error("[getFutureWeather] Unexpected exception. msg=${e.message}", e)
            throw e
        }
    }

    fun getRecommend(request: UserTags): TagRecommendations? {
        val response = try {
            api.getTag(TagRequest(request))
        } catch (e: FeignException) {
            log.warn("[getRecommend] FeignException occurred. Returning null. msg=${e.message}")
            return null
        } catch (e: Exception) {
            log.error("[getRecommend] Unexpected exception occurred. Returning null. msg=${e.message}", e)
            return null
        }

        if (response.size != 3) {
            log.warn("[getRecommend] Response size is not 3 (actual: ${response.size}). Returning null.")
            return null
        }

        return TagRecommendations(
            firstRecommendTag = TagRecommendation(response[0].content),
            secondRecommendTag = TagRecommendation(response[1].content),
            thirdRecommendTag = TagRecommendation(response[2].content),
        )
    }

    fun getOnlyNewRecommend(request: UserTags): TagRecommendations? {
        val response = try {
            api.getTagOnlyNew(TagRequest(request))
        } catch (e: FeignException) {
            log.warn("[getOnlyNewRecommend] FeignException occurred. Returning null. msg=${e.message}")
            return null
        } catch (e: Exception) {
            log.error("[getOnlyNewRecommend] Unexpected exception occurred. Returning null. msg=${e.message}", e)
            return null
        }

        if (response.size != 3) {
            log.warn("[getOnlyNewRecommend] Response size is not 3 (actual: ${response.size}). Returning null.")
            return null
        }

        return TagRecommendations(
            firstRecommendTag = TagRecommendation(response[0].content),
            secondRecommendTag = TagRecommendation(response[1].content),
            thirdRecommendTag = TagRecommendation(response[2].content),
        )
    }

    fun getSandwich(request: SandwichRequest): SandwichResponse? {
        return try {
            api.getSandwich(request)
        } catch (e: FeignException) {
            log.warn("[getSandwich] FeignException, empty 반환. msg=${e.message}")
            return null
        } catch (e: Exception) {
            log.error("[getSandwich] 예기치 못한 예외, empty 반환.", e)
            return null
        }
    }
}

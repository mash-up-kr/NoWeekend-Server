package noweekend.client.mcp.recommend

import feign.FeignException
import noweekend.client.mcp.recommend.model.TagApiResponses
import noweekend.client.mcp.recommend.model.TagRequest
import noweekend.client.mcp.recommend.model.TagResponse
import noweekend.client.mcp.recommend.model.WeatherRequest
import noweekend.client.mcp.recommend.model.WeatherResponse
import noweekend.core.domain.tag.UserTags
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class RecommendClient(
    private val api: RecommendApi,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun getFutureWeather(request: WeatherRequest): List<WeatherResponse> {
        try {
            val response = api.getFutureWeather(request)
            return response.body ?: emptyList()
        } catch (e: FeignException) {
            log.warn("[getFutureWeather] FeignException occurred. Returning empty list. msg=${e.message}")
            return emptyList()
        } catch (e: Exception) {
            log.error("[getFutureWeather] Unexpected exception occurred. Returning empty list. msg=${e.message}", e)
            return emptyList()
        }
    }

    fun getRecommend(request: UserTags): TagApiResponses? {
        val response = try {
            api.getTag(TagRequest(request))
        } catch (e: FeignException) {
            log.warn("[getRecommend] FeignException occurred. Returning null. msg=${e.message}")
            return null
        } catch (e: Exception) {
            log.error("[getRecommend] Unexpected exception occurred. Returning null. msg=${e.message}", e)
            return null
        }

        val tagResponse = response.body ?: run {
            log.warn("[getRecommend] Response body is null. Returning null.")
            return null
        }

        if (tagResponse.size != 3) {
            log.warn("[getRecommend] Response size is not 3 (actual: ${tagResponse.size}). Returning null.")
            return null
        }

        return TagApiResponses(
            firstRecommendTag = TagResponse(tagResponse[0].content),
            secondRecommendTag = TagResponse(tagResponse[1].content),
            thirdRecommendTag = TagResponse(tagResponse[2].content),
        )
    }

    fun getOnlyNewRecommend(request: UserTags): TagApiResponses? {
        val response = try {
            api.getTagOnlyNew(TagRequest(request))
        } catch (e: FeignException) {
            log.warn("[getOnlyNewRecommend] FeignException occurred. Returning null. msg=${e.message}")
            return null
        } catch (e: Exception) {
            log.error("[getOnlyNewRecommend] Unexpected exception occurred. Returning null. msg=${e.message}", e)
            return null
        }

        val tagResponse = response.body ?: run {
            log.warn("[getOnlyNewRecommend] Response body is null. Returning null.")
            return null
        }

        if (tagResponse.size != 3) {
            log.warn("[getOnlyNewRecommend] Response size is not 3 (actual: ${tagResponse.size}). Returning null.")
            return null
        }

        return TagApiResponses(
            firstRecommendTag = TagResponse(tagResponse[0].content),
            secondRecommendTag = TagResponse(tagResponse[1].content),
            thirdRecommendTag = TagResponse(tagResponse[2].content),
        )
    }
}

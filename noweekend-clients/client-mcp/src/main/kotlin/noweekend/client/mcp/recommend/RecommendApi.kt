package noweekend.client.mcp.recommend

import noweekend.client.mcp.recommend.model.AiGenerateVacationRequest
import noweekend.client.mcp.recommend.model.AiGenerateVacationResponse
import noweekend.client.mcp.recommend.model.BridgeVacationPeriod
import noweekend.client.mcp.recommend.model.SandwichRequest
import noweekend.client.mcp.recommend.model.TagRequest
import noweekend.client.mcp.recommend.model.WeatherRequest
import noweekend.core.domain.tag.TagRecommendation
import noweekend.core.domain.weather.WeatherRecommendation
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(
    name = "mcp-host",
    url = "\${mcp.host.url}",
)
interface RecommendApi {
    @RequestMapping(
        value = ["/getFutureWeather"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        method = [RequestMethod.POST],
    )
    fun getFutureWeather(@RequestBody req: WeatherRequest): List<WeatherRecommendation>

    @RequestMapping(
        value = ["/getTag"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        method = [RequestMethod.POST],
    )
    fun getTag(@RequestBody request: TagRequest): List<TagRecommendation>

    @RequestMapping(
        value = ["/getTagOnlyNew"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        method = [RequestMethod.POST],
    )
    fun getTagOnlyNew(@RequestBody request: TagRequest): List<TagRecommendation>

    @RequestMapping(
        value = ["/getSandwich"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        method = [RequestMethod.POST],
    )
    fun getSandwich(@RequestBody request: SandwichRequest): List<BridgeVacationPeriod>

    @RequestMapping(
        value = ["/generate-vacation"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        method = [RequestMethod.POST],
    )
    fun generateVacation(@RequestBody request: AiGenerateVacationRequest): AiGenerateVacationResponse
}

package noweekend.client.mcp.recommend

import noweekend.client.mcp.recommend.model.SandwichRequest
import noweekend.client.mcp.recommend.model.SandwichResponse
import noweekend.client.mcp.recommend.model.TagRequest
import noweekend.client.mcp.recommend.model.TagResponse
import noweekend.client.mcp.recommend.model.WeatherRequest
import noweekend.client.mcp.recommend.model.WeatherResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
    fun getFutureWeather(@RequestBody req: WeatherRequest): ResponseEntity<List<WeatherResponse>>

    @RequestMapping(
        value = ["/getTag"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        method = [RequestMethod.POST],
    )
    fun getTag(@RequestBody request: TagRequest): ResponseEntity<List<TagResponse>>

    @RequestMapping(
        value = ["/getTagOnlyNew"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        method = [RequestMethod.POST],
    )
    fun getTagOnlyNew(@RequestBody request: TagRequest): ResponseEntity<List<TagResponse>>

    @RequestMapping(
        value = ["/getSandwich"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        method = [RequestMethod.POST],
    )
    fun getSandwich(@RequestBody request: SandwichRequest): ResponseEntity<SandwichResponse>
}

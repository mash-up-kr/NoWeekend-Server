package noweekend.client.weather

import noweekend.client.weather.model.WeatherRequest
import noweekend.client.weather.model.WeatherResponse
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
interface WeatherRecommendApi {
    @RequestMapping(
        value = ["/getFutureWeather"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        method = [RequestMethod.POST],
    )
    fun getFutureWeather(@RequestBody req: WeatherRequest): ResponseEntity<List<WeatherResponse>>
}

package noweekend.mcpserver.controller

import noweekend.mcpserver.domain.LocationRequest
import noweekend.mcpserver.tool.FutureWeatherTool
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Deprecated(message = "테스트 전용")
@RestController
class WeatherController(
    private val futureWeatherTool: FutureWeatherTool,
) {

    @GetMapping("/test")
    fun test() {
        // 서울 위도, 경도 샘플
        val request = LocationRequest(
            latitude = 37.4659,
            longitude = 126.3131,
        )
        val rawKmaForecast = futureWeatherTool.getRainSnowSummaryByDay(request)
        for (entry in rawKmaForecast) {
            println(entry)
        }
    }
}

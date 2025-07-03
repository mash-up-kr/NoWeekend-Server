package noweekend.mcpserver.client

import noweekend.mcpserver.domain.KmaForecastResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "kmaWeatherClient",
    url = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0",
)
interface KmaWeatherClient {
    @GetMapping("/getVilageFcst")
    fun getForecast(
        @RequestParam("serviceKey") serviceKey: String,
        @RequestParam("dataType") dataType: String = "JSON",
        @RequestParam("base_date") baseDate: String,
        @RequestParam("base_time") baseTime: String,
        @RequestParam("nx") nx: Int,
        @RequestParam("ny") ny: Int,
        @RequestParam("numOfRows") numOfRows: Int = 1000,
        @RequestParam("pageNo") pageNo: Int = 1,
    ): KmaForecastResponse
}

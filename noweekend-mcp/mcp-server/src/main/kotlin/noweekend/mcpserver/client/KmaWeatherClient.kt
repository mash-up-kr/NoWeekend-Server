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
    /**
     * Retrieves weather forecast data from the Korea Meteorological Administration (KMA) API for a specified location and time.
     *
     * @param serviceKey The API authentication key.
     * @param dataType The response format (e.g., "JSON"). Defaults to "JSON".
     * @param baseDate The base date for the forecast, in YYYYMMDD format.
     * @param baseTime The base time for the forecast, in HHMM format.
     * @param nx The X coordinate of the forecast grid.
     * @param ny The Y coordinate of the forecast grid.
     * @param numOfRows The number of rows to retrieve. Defaults to 1000.
     * @param pageNo The page number for pagination. Defaults to 1.
     * @return The weather forecast response from the KMA API.
     */
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

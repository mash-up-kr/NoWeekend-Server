package noweekend.mcpserver.client

import noweekend.mcpserver.tool.dto.ClimateResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "climateClient", url = "https://climate-api.open-meteo.com")
interface ClimateClient {

    @GetMapping("/v1/climate")
    fun getClimate(
        @RequestParam("latitude") latitude: Double,
        @RequestParam("longitude") longitude: Double,
        @RequestParam("start_date") startDate: String,
        @RequestParam("end_date") endDate: String,
        @RequestParam("models") models: String,
        @RequestParam("daily") dailyVars: String,
        @RequestParam("timezone") timezone: String
    ): ClimateResponse
}
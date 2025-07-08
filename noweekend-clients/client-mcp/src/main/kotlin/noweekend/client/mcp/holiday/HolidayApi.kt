package noweekend.client.mcp.holiday

import noweekend.client.mcp.holiday.model.HolidayRootDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "holidayFeignClient",
    url = "https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService",
)
interface HolidayApi {
    @GetMapping("/getHoliDeInfo")
    fun getHolidays(
        @RequestParam("solYear") solYear: Int,
        @RequestParam("solMonth") solMonth: String,
        @RequestParam("ServiceKey") serviceKey: String,
        @RequestParam("_type") type: String = "json",
    ): HolidayRootDto
}

package noweekend.client.mcp.holiday

import noweekend.client.mcp.holiday.model.HolidayResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "holidayApi",
    url = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService",
)
interface HolidayApi {
    @GetMapping(
        value = ["/getRestDeInfo"],
        produces = [MediaType.APPLICATION_XML_VALUE],
    )
    fun getRestDeInfo(
        @RequestParam("serviceKey") serviceKey: String,
        @RequestParam("solYear") solYear: String,
        @RequestParam("solMonth") solMonth: String,
    ): HolidayResponse
}

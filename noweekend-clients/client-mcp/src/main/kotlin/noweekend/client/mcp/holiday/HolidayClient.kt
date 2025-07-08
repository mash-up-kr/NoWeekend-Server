package noweekend.client.mcp.holiday

import noweekend.client.mcp.holiday.model.HolidayItemDto
import noweekend.client.mcp.holiday.model.HolidayRequest
import noweekend.client.mcp.properties.KmaApiProperties
import org.springframework.stereotype.Component

@Component
class HolidayClient(
    private val api: HolidayApi,
    private val kmaApiProperties: KmaApiProperties,
) {
    fun getHolidays(request: HolidayRequest): List<HolidayItemDto> {
        val resp = api.getRestDeInfo(
            kmaApiProperties.key,
            request.solYear,
            request.solMonth,
        )

        if (resp.header?.resultCode != "00") {
            return emptyList()
        }

        return resp.body?.items?.item.orEmpty()
    }
}

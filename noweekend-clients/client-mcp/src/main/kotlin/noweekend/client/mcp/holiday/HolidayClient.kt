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
        val formattedMonth = String.format("%02d", request.month)
        val dto = api.getHolidays(
            solYear = request.year,
            solMonth = formattedMonth,
            serviceKey = kmaApiProperties.key,
            type = "json",
        )
        return dto.response?.body?.items?.item ?: emptyList()
    }
}

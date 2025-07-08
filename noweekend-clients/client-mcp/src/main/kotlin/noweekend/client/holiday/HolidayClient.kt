package noweekend.client.holiday

import noweekend.client.holiday.model.HolidayItemDto
import noweekend.client.holiday.model.HolidayRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class HolidayClient(
    private val api: HolidayApi,
    @Value("\${kma.api.key}") private val apiKey: String,
) {

    fun getHolidays(request: HolidayRequest): List<HolidayItemDto> {
        val formattedMonth = String.format("%02d", request.month)
        val dto = api.getHolidays(
            solYear = request.year,
            solMonth = formattedMonth,
            serviceKey = apiKey,
            type = "json"
        )
        return dto.response?.body?.items?.item ?: emptyList()
    }

}

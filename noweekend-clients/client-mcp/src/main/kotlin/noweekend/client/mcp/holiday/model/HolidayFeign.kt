package noweekend.client.mcp.holiday.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import noweekend.core.domain.holiday.DayOfWeekKor
import noweekend.core.domain.holiday.Holiday
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// 요청 파라미터 DTO
data class HolidayRequest(
    val year: Int,
    val month: Int,
) {
    val solYear: String = year.toString()
    val solMonth: String = month.toString().padStart(2, '0')
}

// API 응답 item 하나를 표현
data class HolidayItemDto(
    @JacksonXmlProperty(localName = "locdate")
    val date: String,

    @JacksonXmlProperty(localName = "dateName")
    val name: String,
) {
    private companion object {
        val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    }

    fun toDomain(requestYear: Int): Holiday {
        val localDate = LocalDate.parse(date, DATE_FORMAT)
        val dowKor = DayOfWeekKor.valueOf(localDate.dayOfWeek.name.take(3))
        return Holiday.register(
            year = requestYear,
            month = localDate.monthValue,
            day = localDate.dayOfMonth,
            content = name,
            dayOfWeekKor = dowKor,
        )
    }
}

// 전체 XML 응답을 표현
@JacksonXmlRootElement(localName = "response")
data class HolidayResponse(
    @JacksonXmlProperty(localName = "header")
    val header: ResponseHeader? = null,

    @JacksonXmlProperty(localName = "body")
    val body: ResponseBody? = null,
) {
    data class ResponseHeader(
        @JacksonXmlProperty(localName = "resultCode")
        val resultCode: String? = null,

        @JacksonXmlProperty(localName = "resultMsg")
        val resultMsg: String? = null,
    )

    data class ResponseBody(
        @JacksonXmlProperty(localName = "items")
        val items: Items? = null,
    ) {
        data class Items(
            @JacksonXmlElementWrapper(useWrapping = false)
            @JacksonXmlProperty(localName = "item")
            val item: List<HolidayItemDto> = emptyList(),
        )
    }
}

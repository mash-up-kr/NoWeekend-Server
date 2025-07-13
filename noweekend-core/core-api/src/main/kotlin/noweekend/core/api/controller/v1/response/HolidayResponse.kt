package noweekend.core.api.controller.v1.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import noweekend.core.domain.holiday.DayOfWeekKor
import noweekend.core.domain.holiday.Holiday
import java.time.LocalDate

data class HolidayResponse(
    @get:JsonProperty("date")
    @get:JsonFormat(pattern = "yyyy-MM-dd")
    val date: LocalDate,

    val content: String,

    val dayOfWeekKor: DayOfWeekKor,
) {

    companion object {
        fun from(holiday: Holiday): HolidayResponse = HolidayResponse(
            date = LocalDate.of(holiday.year, holiday.month, holiday.day),
            content = holiday.content,
            dayOfWeekKor = holiday.dayOfWeekKor,
        )
    }
}

data class HolidayResponses(
    val holidays: List<HolidayResponse>,
)

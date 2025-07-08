package noweekend.core.api.controller.v1.response

import noweekend.core.domain.holiday.DayOfWeekKor
import noweekend.core.domain.holiday.Holiday

data class HolidayResponse(
    val year: Int,
    val month: Int,
    val content: String,
    val dayOfWeekKor: DayOfWeekKor,
) {
    companion object {
        fun from(holiday: Holiday): HolidayResponse {
            return HolidayResponse(
                year = holiday.year,
                month = holiday.month,
                content = holiday.content,
                dayOfWeekKor = holiday.dayOfWeekKor,
            )
        }
    }
}

data class HolidayResponses(
    val holidays: List<HolidayResponse>,
)

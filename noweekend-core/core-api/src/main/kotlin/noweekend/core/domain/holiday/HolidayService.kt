package noweekend.core.domain.holiday

import noweekend.core.api.controller.v1.response.HolidayResponse

interface HolidayService {
    fun getThisYearHolidays(): List<HolidayResponse>
    fun updateHolidays(year: Int)
}

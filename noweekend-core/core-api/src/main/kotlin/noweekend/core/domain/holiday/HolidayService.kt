package noweekend.core.domain.holiday

import noweekend.core.api.controller.v1.response.HolidayResponse

interface HolidayService {
    fun updateHolidays(year: Int)
    fun getRemainingHolidays(): List<HolidayResponse>
    fun getMonthHolidays(year: Int, month: Int): List<HolidayResponse>
}

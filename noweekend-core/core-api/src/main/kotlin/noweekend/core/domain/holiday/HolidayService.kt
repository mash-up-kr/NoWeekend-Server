package noweekend.core.domain.holiday

import noweekend.core.api.controller.v1.response.HolidayResponse

interface HolidayService {
    fun updateHolidays(year: Int)
    fun getThisMonthHolidays(): List<HolidayResponse>
    fun getRemainingHolidays(): List<HolidayResponse>
}

package noweekend.core.domain.holuday

import noweekend.core.domain.holiday.HolidayServiceImpl
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class HolidayScheduler(
    private val holidayService: HolidayServiceImpl
) {
    @Scheduled(cron = "0 0 1 * * *")
    fun syncTodayYearHolidays() {
        val year = LocalDate.now().year
        holidayService.updateHolidays(year)
    }
}
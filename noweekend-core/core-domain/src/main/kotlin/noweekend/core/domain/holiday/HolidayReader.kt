package noweekend.core.domain.holiday

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
@Transactional(readOnly = true)
class HolidayReader(
    private val holidayRepository: HolidayRepository,
) {

    fun findAllByYear(year: Int): List<Holiday> {
        return holidayRepository.findAllByYear(year)
    }

    fun findMonthHolidays(year: Int, month: Int): List<Holiday> {
        return holidayRepository.findAllByYear(year)
            .filter { it.month == month }
            .sortedBy { it.day }
    }

    fun findRemainingHolidays(from: LocalDate): List<Holiday> {
        val year = from.year
        return holidayRepository.findAllByYear(year)
            .filter {
                val date = LocalDate.of(it.year, it.month, it.day)
                date.isAfter(from) || date.isEqual(from)
            }
            .sortedWith(compareBy({ it.month }, { it.day }))
    }
}

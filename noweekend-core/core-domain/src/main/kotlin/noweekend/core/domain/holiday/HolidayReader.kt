package noweekend.core.domain.holiday

import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
class HolidayReader(
    private val holidayRepository: HolidayRepository,
) {

    fun findAllByYear(year: Int): List<Holiday> {
        return holidayRepository.findAllByYear(year)
    }
}
package noweekend.core.domain.holiday

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class HolidayReader(
    private val holidayRepository: HolidayRepository,
) {

    fun findAllByYear(year: Int): List<Holiday> {
        return holidayRepository.findAllByYear(year)
    }
}

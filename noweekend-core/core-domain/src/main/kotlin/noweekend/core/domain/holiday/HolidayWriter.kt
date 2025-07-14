package noweekend.core.domain.holiday

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class HolidayWriter(
    private val holidayRepository: HolidayRepository,
) {

    fun register(holiday: Holiday) {
        holidayRepository.save(holiday)
    }
}

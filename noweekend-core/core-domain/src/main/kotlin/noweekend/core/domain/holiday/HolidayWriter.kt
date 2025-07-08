package noweekend.core.domain.holiday

import org.springframework.transaction.annotation.Transactional

@Transactional
class HolidayWriter(
    private val holidayRepository: HolidayRepository,
) {

    fun register(holiday: Holiday) {
        holidayRepository.save(holiday)
    }
}
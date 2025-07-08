package noweekend.storage.db.core.holiday

import noweekend.core.domain.holiday.Holiday
import noweekend.core.domain.holiday.HolidayRepository
import org.springframework.stereotype.Repository

@Repository
class HolidayCoreRepository(
    private val holidayJpaRepository: HolidayJpaRepository,
) : HolidayRepository {

    override fun findAllByYear(year: Int): List<Holiday> {
        return holidayJpaRepository.findAllByYear(year)
            .map { it.toDomain() }
    }

    override fun save(holiday: Holiday) {
        holidayJpaRepository.save(holiday.toEntity())
    }
}

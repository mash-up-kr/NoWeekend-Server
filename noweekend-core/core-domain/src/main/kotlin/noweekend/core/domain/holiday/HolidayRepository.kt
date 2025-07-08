package noweekend.core.domain.holiday

interface HolidayRepository {
    fun findAllByYear(year: Int): List<Holiday>
    fun save(holiday: Holiday)
}

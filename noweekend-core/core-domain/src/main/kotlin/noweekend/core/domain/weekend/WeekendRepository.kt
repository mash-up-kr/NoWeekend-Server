package noweekend.core.domain.weekend

import java.time.LocalDate

interface WeekendRepository {
    fun register(weekend: Weekend)
    fun findByYear(year: Int): List<Weekend>
    fun findAfterDate(date: LocalDate): List<Weekend>
}

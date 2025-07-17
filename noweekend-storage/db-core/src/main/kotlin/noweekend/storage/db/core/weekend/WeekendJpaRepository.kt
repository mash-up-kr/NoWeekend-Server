package noweekend.storage.db.core.weekend

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface WeekendJpaRepository : JpaRepository<WeekendEntity, String> {
    fun findAllByDateBetween(start: LocalDate, end: LocalDate): List<WeekendEntity>
    fun findAllByDateAfter(date: LocalDate): List<WeekendEntity>
}

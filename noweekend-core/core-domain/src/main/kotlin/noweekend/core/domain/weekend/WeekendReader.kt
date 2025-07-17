package noweekend.core.domain.weekend

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
@Transactional(readOnly = true)
class WeekendReader(
    private val weekendRepository: WeekendRepository,
) {

    fun getAllThisYearWeekends(): List<Weekend> {
        val year = LocalDate.now().year
        return weekendRepository.findByYear(year)
    }

    fun getUpcomingWeekends(): List<Weekend> {
        val now = LocalDate.now()
        return weekendRepository.findAfterDate(now)
    }
}

package noweekend.core.domain.weekend

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class WeekendScheduler(
    private val weekendWriter: WeekendWriter,
) {

    @Scheduled(cron = "0 0 0 1 1 *")
    fun generateThisYearWeekends() {
        val year = LocalDate.now().year
        val start = LocalDate.of(year, 1, 1)
        val end = LocalDate.of(year, 12, 31)
        var date = start
        val weekends = mutableListOf<Weekend>()

        while (!date.isAfter(end)) {
            if (date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY) {
                weekends.add(
                    Weekend.generate(
                        date = date,
                        dayOfWeek = date.dayOfWeek,
                    ),
                )
            }
            date = date.plusDays(1)
        }

        weekends.forEach { weekendWriter.register(it) }
    }
}

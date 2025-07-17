package noweekend.core.domain.weekend

import noweekend.core.domain.util.IdGenerator
import java.time.DayOfWeek
import java.time.LocalDate

data class Weekend(
    val id: String,
    val date: LocalDate,
    val dayOfWeek: DayOfWeek,
) {
    companion object {
        fun generate(date: LocalDate, dayOfWeek: DayOfWeek): Weekend {
            return Weekend(
                id = IdGenerator.generate(),
                date = date,
                dayOfWeek = dayOfWeek,
            )
        }
    }
}

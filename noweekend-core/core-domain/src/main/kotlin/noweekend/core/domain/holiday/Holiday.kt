package noweekend.core.domain.holiday

import noweekend.core.domain.util.IdGenerator
import java.time.LocalDateTime

class Holiday(
    val id: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val content: String,
    val dayOfWeekKor: DayOfWeekKor,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun register(
            year: Int,
            month: Int,
            day: Int,
            content: String,
            dayOfWeekKor: DayOfWeekKor,
        ): Holiday {
            return Holiday(
                id = IdGenerator.generate(),
                year = year,
                month = month,
                day = day,
                content = content,
                dayOfWeekKor = dayOfWeekKor,
                updatedAt = LocalDateTime.now(),
            )
        }
    }
}

enum class DayOfWeekKor(val display: String) {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일"),
}

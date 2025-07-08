package noweekend.core.domain.holiday

import noweekend.core.domain.util.IdGenerator
import java.time.LocalDateTime

class Holiday(
    val id: String,
    val year: Int,
    val month: Int,
    val content: String,
    val dayOfWeekKor: DayOfWeekKor,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun register(
            year: Int,
            month: Int,
            content: String,
            dayOfWeekKor: DayOfWeekKor,
            updatedAt: LocalDateTime = LocalDateTime.now()
        ): Holiday {
            return Holiday(
                id = IdGenerator.generate(),
                year = year,
                month = month,
                content = content,
                dayOfWeekKor = dayOfWeekKor,
                updatedAt = updatedAt
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
    ;
}
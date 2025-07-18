package noweekend.core.domain.vacation

import noweekend.core.domain.util.IdGenerator
import java.time.LocalDate

class AiVacation(
    val id: String,
    val title: String,
    val content: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val searchDate: LocalDate,
    val userId: String,
    val iconStyle: IconStyle,
) {
    companion object {
        fun register(
            title: String,
            content: String,
            startDate: LocalDate,
            endDate: LocalDate,
            searchDate: LocalDate,
            userId: String,
            iconStyle: IconStyle,
        ): AiVacation {
            return AiVacation(
                id = IdGenerator.generate(),
                title = title,
                content = content,
                startDate = startDate,
                endDate = endDate,
                searchDate = searchDate,
                userId = userId,
                iconStyle = iconStyle,
            )
        }
    }
}

enum class IconStyle {
    STAR,
    TRAIN,
    PLANE,
    HOUSE,
}

package noweekend.core.api.controller.v1.response

import noweekend.core.domain.vacation.IconStyle
import java.time.LocalDate

data class AiVacationApiResponse(
    val title: String,
    val content: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val iconStyle: IconStyle,
)

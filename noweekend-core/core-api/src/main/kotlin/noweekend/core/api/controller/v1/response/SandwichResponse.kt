package noweekend.core.api.controller.v1.response

import java.time.LocalDate

data class SandwichResponse(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val useAnnualLeave: Int,
    val totalVacationDays: Int,
)

data class SandwichApiResponse(
    val responses: List<SandwichResponse>,
)

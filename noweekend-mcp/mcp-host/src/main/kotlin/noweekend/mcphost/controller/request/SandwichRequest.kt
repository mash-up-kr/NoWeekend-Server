package noweekend.mcphost.controller.request

import java.time.LocalDate

data class SandwichRequest(
    val birthDay: LocalDate,
    val holidays: List<LocalDate>,
    val remainingAnnualLeave: Int,
    val weekends: List<LocalDate>,
)

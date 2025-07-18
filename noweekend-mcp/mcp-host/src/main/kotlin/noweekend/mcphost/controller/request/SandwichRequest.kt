package noweekend.mcphost.controller.request

import java.time.LocalDate

data class SandwichRequest(
    val holidays: List<LocalDate>,
    val weekends: List<LocalDate>,
)

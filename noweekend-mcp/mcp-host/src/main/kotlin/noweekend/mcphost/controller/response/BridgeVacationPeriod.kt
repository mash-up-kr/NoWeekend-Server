package noweekend.mcphost.controller.response

import java.time.LocalDate

data class BridgeVacationPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
)
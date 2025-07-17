package noweekend.client.mcp.recommend.model

import java.time.LocalDate

data class SandwichRequest(
    val birthDay: LocalDate,
    val holidays: List<LocalDate>,
    val remainingAnnualLeave: Int,
    val weekends: List<LocalDate>,
)

data class BridgeVacationPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
)

data class SandwichResponse(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val useAnnualLeave: Int,
    val totalVacationDays: Int,
)

data class SandwichApiResponse(
    val responses: List<SandwichResponse>,
)

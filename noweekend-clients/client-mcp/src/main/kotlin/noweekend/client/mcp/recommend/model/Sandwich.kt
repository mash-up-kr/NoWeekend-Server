package noweekend.client.mcp.recommend.model

import java.time.LocalDate

data class SandwichRequest(
    val birthDay: LocalDate,
    val holidays: List<LocalDate>,
)

data class SandwichResponse(
    val startDate: LocalDate,
    val endDate: LocalDate,
)

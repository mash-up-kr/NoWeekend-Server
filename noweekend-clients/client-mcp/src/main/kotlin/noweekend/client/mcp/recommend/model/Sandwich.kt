package noweekend.client.mcp.recommend.model

import java.time.LocalDate

data class SandwichRequest(
    val birthDay: LocalDate,
    val holidays: List<LocalDate>,
)

data class SandwichResponse(
    val content: String,
)

data class SandwichResponses(
    val contents: List<SandwichResponse>,
)

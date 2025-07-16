package noweekend.mcphost.controller.response

import java.time.LocalDate

data class SandwichResponse(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val totalDays: Int,
    val usedAnnualLeaveDates: List<LocalDate>
)

data class SandwichResult(
    val useAnnualLeave4: List<SandwichResponse> = emptyList(),
    val useAnnualLeave3: List<SandwichResponse> = emptyList(),
    val useAnnualLeave2: List<SandwichResponse> = emptyList(),
    val useAnnualLeave1: List<SandwichResponse> = emptyList()
)

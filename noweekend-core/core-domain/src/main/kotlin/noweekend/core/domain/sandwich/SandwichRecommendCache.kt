package noweekend.core.domain.sandwich

import noweekend.core.domain.util.IdGenerator
import java.time.LocalDate
import java.time.LocalDateTime

data class SandwichRecommendCache(
    val id: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val searchDate: LocalDateTime,
    val useAnnualLeave: Int,
    val totalVacationDays: Int,
) {
    companion object {
        fun register(
            startDate: LocalDate,
            endDate: LocalDate,
            searchDate: LocalDateTime,
            useAnnualLeave: Int,
            totalVacationDays: Int,
        ): SandwichRecommendCache {
            val fixedSearchDate = searchDate.withMinute(0).withSecond(0).withNano(0)
            return SandwichRecommendCache(
                id = IdGenerator.generate(),
                startDate = startDate,
                endDate = endDate,
                searchDate = fixedSearchDate,
                useAnnualLeave = useAnnualLeave,
                totalVacationDays = totalVacationDays,
            )
        }
    }
}

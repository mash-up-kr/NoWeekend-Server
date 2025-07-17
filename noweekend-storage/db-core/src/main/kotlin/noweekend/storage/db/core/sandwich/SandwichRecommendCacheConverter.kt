package noweekend.storage.db.core.sandwich

import noweekend.core.domain.sandwich.SandwichRecommendCache
import org.springframework.stereotype.Component

@Component
class SandwichRecommendCacheConverter {
    fun entityToDomain(entity: SandwichRecommendCacheEntity): SandwichRecommendCache =
        SandwichRecommendCache(
            id = entity.id,
            startDate = entity.startDate,
            endDate = entity.endDate,
            searchDate = entity.searchDate,
            useAnnualLeave = entity.useAnnualLeave,
            totalVacationDays = entity.totalVacationDays,
        )

    fun domainToEntity(domain: SandwichRecommendCache): SandwichRecommendCacheEntity =
        SandwichRecommendCacheEntity(
            id = domain.id,
            startDate = domain.startDate,
            endDate = domain.endDate,
            searchDate = domain.searchDate,
            useAnnualLeave = domain.useAnnualLeave,
            totalVacationDays = domain.totalVacationDays,
        )
}

package noweekend.core.domain.sandwich

import java.time.LocalDateTime

interface SandwichRecommendCacheRepository {
    fun findAllBySearchDate(searchDate: LocalDateTime): List<SandwichRecommendCache>
    fun registerAll(caches: List<SandwichRecommendCache>)
}

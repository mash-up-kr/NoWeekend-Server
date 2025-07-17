package noweekend.storage.db.core.sandwich

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface SandwichRecommendJpaRepository : JpaRepository<SandwichRecommendCacheEntity, String> {
    fun findAllBySearchDate(searchDate: LocalDateTime): List<SandwichRecommendCacheEntity>
}

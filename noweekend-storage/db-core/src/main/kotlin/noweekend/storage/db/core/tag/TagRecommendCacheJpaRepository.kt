package noweekend.storage.db.core.tag

import noweekend.core.domain.tag.RecommendType
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface TagRecommendCacheJpaRepository : JpaRepository<TagRecommendCacheEntity, String> {
    fun findByUserIdAndSearchDateAndRecommendType(
        userId: String,
        searchDate: LocalDate,
        recommendType: RecommendType,
    ): TagRecommendCacheEntity?
}

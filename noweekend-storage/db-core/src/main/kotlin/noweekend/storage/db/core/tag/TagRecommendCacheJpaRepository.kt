package noweekend.storage.db.core.tag

import noweekend.core.domain.tag.RecommendType
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface TagRecommendCacheJpaRepository : JpaRepository<TagRecommendCacheEntity, String> {
    fun findByRecommendTypeAndSearchDateAndTagsJson(
        recommendType: RecommendType,
        searchDate: LocalDate,
        tagsJson: String,
    ): TagRecommendCacheEntity?
}

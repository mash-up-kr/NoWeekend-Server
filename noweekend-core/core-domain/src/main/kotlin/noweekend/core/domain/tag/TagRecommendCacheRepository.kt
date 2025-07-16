package noweekend.core.domain.tag

import java.time.LocalDate

interface TagRecommendCacheRepository {
    fun findTodayCache(recommendType: RecommendType, tagsJson: String, searchDate: LocalDate): TagRecommendCache?
    fun register(tagRecommendCache: TagRecommendCache)
}

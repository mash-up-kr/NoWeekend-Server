package noweekend.core.domain.tag

import java.time.LocalDate

interface TagRecommendCacheRepository {
    fun findTodayCache(recommendType: RecommendType, searchDate: LocalDate, userId: String): TagRecommendCache?
    fun register(tagRecommendCache: TagRecommendCache)
}

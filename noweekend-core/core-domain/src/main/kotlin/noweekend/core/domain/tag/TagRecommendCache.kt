package noweekend.core.domain.tag

import noweekend.core.domain.util.IdGenerator
import java.time.LocalDate
import java.time.LocalDateTime

data class TagRecommendCache(
    val id: String,
    val recommendType: RecommendType,
    val searchDate: LocalDate,
    val tags: UserTags,
    val recommend: TagRecommendations,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val userId: String,
) {
    companion object {
        fun register(
            recommendType: RecommendType,
            searchDate: LocalDate,
            tags: UserTags,
            recommend: TagRecommendations,
            userId: String,
        ): TagRecommendCache {
            val now = LocalDateTime.now()
            return TagRecommendCache(
                id = IdGenerator.generate(),
                recommendType = recommendType,
                searchDate = searchDate,
                tags = tags,
                recommend = recommend,
                createdAt = now,
                updatedAt = now,
                userId = userId,
            )
        }
    }
}

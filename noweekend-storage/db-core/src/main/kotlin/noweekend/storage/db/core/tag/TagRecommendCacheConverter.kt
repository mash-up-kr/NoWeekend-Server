package noweekend.storage.db.core.tag

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import noweekend.core.domain.tag.TagRecommendCache
import noweekend.core.domain.tag.TagRecommendations
import noweekend.core.domain.tag.UserTags
import org.springframework.stereotype.Component

@Component
class TagRecommendCacheConverter(
    private val objectMapper: ObjectMapper,
) {
    fun entityToDomain(entity: TagRecommendCacheEntity): TagRecommendCache =
        TagRecommendCache(
            id = entity.id,
            recommendType = entity.recommendType,
            searchDate = entity.searchDate,
            tags = objectMapper.readValue(entity.tagsJson, object : TypeReference<UserTags>() {}),
            recommend = objectMapper.readValue(entity.recommendJson, TagRecommendations::class.java),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            userId = entity.userId,
        )

    fun domainToEntity(domain: TagRecommendCache): TagRecommendCacheEntity =
        TagRecommendCacheEntity(
            id = domain.id,
            recommendType = domain.recommendType,
            searchDate = domain.searchDate,
            tagsJson = objectMapper.writeValueAsString(domain.tags),
            recommendJson = objectMapper.writeValueAsString(domain.recommend),
            userId = domain.userId,
        )
}

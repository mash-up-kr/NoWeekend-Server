package noweekend.storage.db.core.tag

import noweekend.core.domain.tag.RecommendType
import noweekend.core.domain.tag.TagRecommendCache
import noweekend.core.domain.tag.TagRecommendCacheRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class TagRecommendCacheCoreRepository(
    private val jpaRepository: TagRecommendCacheJpaRepository,
    private val converter: TagRecommendCacheConverter,
) : TagRecommendCacheRepository {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun findTodayCache(
        recommendType: RecommendType,
        tagsJson: String,
        searchDate: LocalDate,
    ): TagRecommendCache? {
        return jpaRepository.findByRecommendTypeAndSearchDateAndTagsJson(
            recommendType = recommendType,
            searchDate = searchDate,
            tagsJson = tagsJson,
        )?.let { converter.entityToDomain(it) }
    }

    override fun register(tagRecommendCache: TagRecommendCache) {
        try {
            jpaRepository.save(converter.domainToEntity(tagRecommendCache))
        } catch (e: DataIntegrityViolationException) {
            log.debug("TagRecommendCache already exists for recommendType, date, tagsJson", e)
        }
    }
}

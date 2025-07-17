package noweekend.storage.db.core.sandwich

import noweekend.core.domain.sandwich.SandwichRecommendCache
import noweekend.core.domain.sandwich.SandwichRecommendCacheRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class SandwichRecommendCoreRepository(
    private val jpaRepository: SandwichRecommendJpaRepository,
    private val converter: SandwichRecommendCacheConverter,
) : SandwichRecommendCacheRepository {

    override fun findAllBySearchDate(searchDate: LocalDateTime): List<SandwichRecommendCache> {
        // 여기서도 시단위로 맞추기 (강제 보장)
        val fixedSearchDate = searchDate.withMinute(0).withSecond(0).withNano(0)
        return jpaRepository.findAllBySearchDate(fixedSearchDate)
            .map { converter.entityToDomain(it) }
    }

    @Transactional
    override fun registerAll(caches: List<SandwichRecommendCache>) {
        jpaRepository.saveAll(caches.map { converter.domainToEntity(it) })
    }
}

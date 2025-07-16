package noweekend.core.domain.tag

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
@Transactional(readOnly = true)
class TagRecommendCacheReader(
    private val repository: TagRecommendCacheRepository,
) {
    fun findTodayCache(
        recommendType: RecommendType,
        userId: String,
    ): TagRecommendCache? {
        return repository.findTodayCache(recommendType, LocalDate.now(), userId)
    }
}

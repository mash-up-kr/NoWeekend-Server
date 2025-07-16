package noweekend.core.domain.tag

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class TagRecommendCacheWriter(
    private val repository: TagRecommendCacheRepository,
) {
    fun register(tagRecommendCache: TagRecommendCache) {
        repository.register(tagRecommendCache)
    }
}

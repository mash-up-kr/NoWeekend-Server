package noweekend.core.domain.sandwich

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class SandwichWriter(
    private val repository: SandwichRecommendCacheRepository,
) {
    fun registerAll(caches: List<SandwichRecommendCache>) {
        repository.registerAll(caches)
    }
}

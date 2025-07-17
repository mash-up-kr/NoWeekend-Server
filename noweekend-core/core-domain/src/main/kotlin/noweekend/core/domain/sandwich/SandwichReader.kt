package noweekend.core.domain.sandwich

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class SandwichReader(
    private val repository: SandwichRecommendCacheRepository,
) {
    fun findBySearchDate(searchDate: LocalDateTime): List<SandwichRecommendCache> {
        return repository.findAllBySearchDate(searchDate)
    }
}

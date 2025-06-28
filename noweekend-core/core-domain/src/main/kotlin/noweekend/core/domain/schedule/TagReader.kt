package noweekend.core.domain.schedule

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class TagReader(
    private val tagRepository: TagRepository,
) {
    fun getDefaultTags(): List<String> {
        return enumValues<BasicTag>().map { it.koreanContent }
    }
}

package noweekend.core.domain.schedule

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class TagWriter(
    private val tagRepository: TagRepository,
) {
    fun registerSelectedBasicTag(tags: List<BasicTag>, userId: String) {
        tags.stream().forEach {
                tag ->
            tagRepository.register(
                Tag.register(tag.koreanContent, userId),
            )
        }
    }
}

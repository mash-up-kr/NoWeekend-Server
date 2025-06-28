package noweekend.storage.db.core.schedule

import noweekend.core.domain.schedule.Tag
import noweekend.core.domain.schedule.TagRepository
import org.springframework.stereotype.Repository

@Repository
class TagHistoryCoreRepository(
    private val tagJpaRepository: TagJpaRepository,
) : TagRepository {
    override fun register(tag: Tag) {
        tagJpaRepository.save(tag.toEntity())
    }
}

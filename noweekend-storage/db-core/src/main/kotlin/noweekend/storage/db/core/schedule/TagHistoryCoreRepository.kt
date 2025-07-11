package noweekend.storage.db.core.schedule

import noweekend.core.domain.tag.Tag
import noweekend.core.domain.tag.TagRepository
import org.springframework.stereotype.Repository

@Repository
class TagHistoryCoreRepository(
    private val tagJpaRepository: TagJpaRepository,
) : TagRepository {
    override fun register(tag: Tag) {
        tagJpaRepository.save(tag.toEntity())
    }

    override fun findAllByUserId(userId: String): List<Tag> {
        return tagJpaRepository.findAllByUserIdAndDeletedFalse(userId).map {
                tagHistoryEntity ->
            tagHistoryEntity.toDomain()
        }
    }

    override fun markDeletedByUserId(userId: String) {
        tagJpaRepository.markDeletedByUserId(userId)
    }
}

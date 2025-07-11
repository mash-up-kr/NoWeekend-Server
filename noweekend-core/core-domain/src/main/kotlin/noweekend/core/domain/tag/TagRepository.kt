package noweekend.core.domain.tag

interface TagRepository {
    fun register(tag: Tag)
    fun findAllByUserId(userId: String): List<Tag>
    fun markDeletedByUserId(userId: String)
}

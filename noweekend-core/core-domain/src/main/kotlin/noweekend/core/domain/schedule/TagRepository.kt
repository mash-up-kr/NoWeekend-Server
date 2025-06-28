package noweekend.core.domain.schedule

interface TagRepository {
    fun register(tag: Tag)
    fun findAllByUserId(userId: String): List<Tag>
}

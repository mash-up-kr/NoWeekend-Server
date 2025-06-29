package noweekend.storage.db.core.schedule

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import noweekend.core.domain.tag.Tag
import noweekend.storage.db.core.BaseEntity

@Entity
@Table(
    name = "tag_history",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_user_content",
            columnNames = ["user_id", "content"],
        ),
    ],
)
class TagHistoryEntity(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "user_id", nullable = false)
    val userId: String,

    @Column(name = "content", length = 50, nullable = false)
    val content: String,

    val selected: Boolean = true,
) : BaseEntity() {
    fun toDomain(): Tag = Tag(
        id = this.id,
        content = this.content,
        userId = this.userId,
        selected = this.selected,
    )
}

fun Tag.toEntity(): TagHistoryEntity = TagHistoryEntity(
    id = this.id,
    userId = this.userId,
    content = this.content,
    selected = this.selected,
)

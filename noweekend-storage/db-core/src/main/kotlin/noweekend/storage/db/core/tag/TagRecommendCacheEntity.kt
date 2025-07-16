package noweekend.storage.db.core.tag

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import noweekend.core.domain.tag.RecommendType
import noweekend.storage.db.core.BaseEntity
import java.time.LocalDate

@Entity
@Table(
    name = "tag_recommend_metadata",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_search_date_type",
            columnNames = ["search_date", "recommend_type"],
        ),
    ],
)
class TagRecommendCacheEntity(
    @Id
    val id: String,

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    val recommendType: RecommendType,

    @Column(nullable = false)
    val searchDate: LocalDate,

    @Column(nullable = false)
    val tagsJson: String,

    @Column(nullable = false)
    val recommendJson: String,
) : BaseEntity()

package noweekend.storage.db.core.vacation

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import noweekend.core.domain.vacation.AiVacation
import noweekend.core.domain.vacation.IconStyle
import java.time.LocalDate

@Entity
@Table(
    name = "ai_vacation",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_ai_vacation_user_searchdate",
            columnNames = ["user_id", "search_date"],
        ),
    ],
)
class AiVacationEntity(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "content", columnDefinition = "TEXT")
    val content: String,

    @Column(name = "start_date")
    val startDate: LocalDate,

    @Column(name = "end_date")
    val endDate: LocalDate,

    @Column(name = "search_date")
    val searchDate: LocalDate,

    @Column(name = "user_id")
    val userId: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "icon_style")
    val iconStyle: IconStyle,
)

// Domain <-> Entity 변환
fun AiVacation.toEntity() = AiVacationEntity(
    id = this.id,
    title = this.title,
    content = this.content,
    startDate = this.startDate,
    endDate = this.endDate,
    searchDate = this.searchDate,
    userId = this.userId,
    iconStyle = this.iconStyle,
)

fun AiVacationEntity.toDomain() = AiVacation(
    id = this.id,
    title = this.title,
    content = this.content,
    startDate = this.startDate,
    endDate = this.endDate,
    searchDate = this.searchDate,
    userId = this.userId,
    iconStyle = this.iconStyle,
)

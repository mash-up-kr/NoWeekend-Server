package noweekend.storage.db.core.weekend

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import noweekend.core.domain.weekend.Weekend
import java.time.DayOfWeek
import java.time.LocalDate

@Entity
@Table(name = "weekend")
class WeekendEntity(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "date", nullable = false)
    val date: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    val dayOfWeek: DayOfWeek,
)

fun Weekend.toEntity(): WeekendEntity =
    WeekendEntity(
        id = this.id,
        date = this.date,
        dayOfWeek = this.dayOfWeek,
    )

fun WeekendEntity.toDomain(): Weekend =
    Weekend(
        id = this.id,
        date = this.date,
        dayOfWeek = this.dayOfWeek,
    )

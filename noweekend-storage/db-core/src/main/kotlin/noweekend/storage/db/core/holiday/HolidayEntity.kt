package noweekend.storage.db.core.holiday

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import noweekend.core.domain.holiday.DayOfWeekKor
import noweekend.core.domain.holiday.Holiday
import noweekend.storage.db.core.BaseEntity

@Entity
@Table(
    name = "holiday",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_holiday_year_month_content_dayofweek",
            columnNames = ["year", "month", "day", "content"],
        ),
    ],
)
class HolidayEntity(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "year")
    val year: Int,

    @Column(name = "month")
    val month: Int,

    @Column(name = "day")
    val day: Int,

    @Column(name = "content")
    val content: String,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "day_of_week_kor")
    val dayOfWeekKor: DayOfWeekKor,
) : BaseEntity()

fun Holiday.toEntity(): HolidayEntity =
    HolidayEntity(
        id = this.id,
        year = this.year,
        month = this.month,
        day = this.day,
        content = this.content,
        dayOfWeekKor = this.dayOfWeekKor,
    )

fun HolidayEntity.toDomain(): Holiday =
    Holiday(
        id = this.id,
        year = this.year,
        month = this.month,
        day = this.day,
        content = this.content,
        dayOfWeekKor = this.dayOfWeekKor,
        updatedAt = this.updatedAt,
    )

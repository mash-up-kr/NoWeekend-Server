package noweekend.storage.db.core.schedule

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import noweekend.core.domain.schedule.Schedule
import noweekend.core.domain.schedule.ScheduleTag
import noweekend.storage.db.core.BaseEntity

@Entity
@Table(name = "schedule")
class ScheduleEntity(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "user_id", nullable = false)
    val userId: String,

    @Column(name = "schedule_tag", length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    val scheduleTag: ScheduleTag,
) : BaseEntity() {
    fun toDomain(): Schedule = Schedule(
        id = this.id,
        tag = this.scheduleTag,
        userId = this.userId,
    )
}

fun Schedule.toEntity(): ScheduleEntity = ScheduleEntity(
    id = this.id,
    userId = this.userId,
    scheduleTag = this.tag,
)

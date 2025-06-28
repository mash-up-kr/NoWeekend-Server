package noweekend.storage.db.core.schedule

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import noweekend.core.domain.enumerate.AlarmOption
import noweekend.core.domain.enumerate.ScheduleCategory
import noweekend.core.domain.schedule.Schedule
import noweekend.storage.db.core.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(
    name = "schedules",
    indexes = [
        Index(name = "idx_schedules_user_id", columnList = "user_id"),
        Index(name = "idx_schedules_user_id_start_time", columnList = "user_id, start_time"),
    ],
)
class ScheduleEntity(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "user_id", length = 50, nullable = false)
    val userId: String,

    @Column(name = "title", length = 200, nullable = false)
    val title: String,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time", nullable = false)
    val endTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 30, nullable = false)
    val category: ScheduleCategory,

    @Column(name = "temperature", nullable = false)
    val temperature: Int,

    @Column(name = "all_day", nullable = false)
    val allDay: Boolean,

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_option", length = 30, nullable = false)
    val alarmOption: AlarmOption,

    @Column(name = "completed", nullable = false)
    val completed: Boolean = false,
) : BaseEntity() {
    fun toSchedule(): Schedule = Schedule(
        id = this.id,
        userId = this.userId,
        title = this.title,
        startTime = this.startTime,
        endTime = this.endTime,
        category = this.category,
        temperature = this.temperature,
        allDay = this.allDay,
        alarmOption = this.alarmOption,
        completed = this.completed,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}

fun Schedule.toEntity(): ScheduleEntity = ScheduleEntity(
    id = this.id,
    userId = this.userId,
    title = this.title,
    startTime = this.startTime,
    endTime = this.endTime,
    category = this.category,
    temperature = this.temperature,
    allDay = this.allDay,
    alarmOption = this.alarmOption,
    completed = this.completed,
)

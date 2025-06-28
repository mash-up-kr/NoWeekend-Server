package noweekend.storage.db.core.schedule

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ScheduleQueryDslRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun findSchedulesByUserIdAndDateRange(
        userId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<ScheduleEntity> {
        val qSchedule = QScheduleEntity.scheduleEntity

        return queryFactory
            .selectFrom(qSchedule)
            .where(
                qSchedule.userId.eq(userId)
                    .and(qSchedule.startTime.goe(startDate))
                    .and(qSchedule.endTime.loe(endDate)),
            )
            .orderBy(qSchedule.startTime.asc())
            .fetch()
    }
}

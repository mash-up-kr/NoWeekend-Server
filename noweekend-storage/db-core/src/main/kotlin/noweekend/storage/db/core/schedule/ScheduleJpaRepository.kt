package noweekend.storage.db.core.schedule

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface ScheduleJpaRepository : JpaRepository<ScheduleEntity, String> {
    fun findByIdAndDeletedFalse(id: String): ScheduleEntity?

    @Modifying
    @Transactional
    @Query("UPDATE ScheduleEntity s SET s.deleted = true WHERE s.userId = :userId")
    fun markDeletedByUserId(@Param("userId") userId: String): Int
}

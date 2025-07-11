package noweekend.storage.db.core.schedule

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TagJpaRepository : JpaRepository<TagHistoryEntity, String> {
    fun findAllByUserIdAndDeletedFalse(userId: String): List<TagHistoryEntity>

    @Modifying
    @Query("UPDATE TagHistoryEntity t SET t.deleted = true WHERE t.userId = :userId")
    fun markDeletedByUserId(@Param("userId") userId: String): Int
}

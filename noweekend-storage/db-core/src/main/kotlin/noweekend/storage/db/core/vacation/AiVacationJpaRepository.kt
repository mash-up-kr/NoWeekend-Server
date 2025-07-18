package noweekend.storage.db.core.vacation

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface AiVacationJpaRepository : JpaRepository<AiVacationEntity, String> {
    fun findAllByUserId(userId: String): List<AiVacationEntity>
    fun findByUserIdAndSearchDate(userId: String, searchDate: LocalDate): AiVacationEntity?
}

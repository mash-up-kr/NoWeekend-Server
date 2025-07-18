package noweekend.core.domain.vacation

import java.time.LocalDate

interface AiVacationRepository {
    fun save(aiVacation: AiVacation)
    fun findByUserIdAndSearchDate(userId: String, searchDate: LocalDate): AiVacation?
    fun findAllByUserId(userId: String): List<AiVacation>
}

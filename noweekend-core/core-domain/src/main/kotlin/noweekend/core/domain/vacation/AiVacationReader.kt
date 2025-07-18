package noweekend.core.domain.vacation

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
@Transactional(readOnly = true)
class AiVacationReader(
    private val aiVacationRepository: AiVacationRepository,
) {
    fun findByUserIdAndSearchDate(userId: String, searchDate: LocalDate): AiVacation? {
        return aiVacationRepository.findByUserIdAndSearchDate(userId, searchDate)
    }
    fun findAllByUserId(userId: String): List<AiVacation> {
        return aiVacationRepository.findAllByUserId(userId)
    }
}

package noweekend.core.domain.vacation

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class AiVacationWriter(
    private val aiVacationRepository: AiVacationRepository,
) {
    fun register(aiVacation: AiVacation) {
        aiVacationRepository.save(aiVacation)
    }
}

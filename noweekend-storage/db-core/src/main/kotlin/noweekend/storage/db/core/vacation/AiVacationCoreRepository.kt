package noweekend.storage.db.core.vacation

import noweekend.core.domain.vacation.AiVacation
import noweekend.core.domain.vacation.AiVacationRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class AiVacationCoreRepository(
    private val aiVacationJpaRepository: AiVacationJpaRepository,
) : AiVacationRepository {

    override fun save(aiVacation: AiVacation) {
        aiVacationJpaRepository.save(aiVacation.toEntity())
    }

    override fun findByUserIdAndSearchDate(userId: String, searchDate: LocalDate): AiVacation? {
        return aiVacationJpaRepository.findByUserIdAndSearchDate(userId, searchDate)?.toDomain()
    }

    override fun findAllByUserId(userId: String): List<AiVacation> {
        return aiVacationJpaRepository.findAllByUserId(userId).map { it.toDomain() }
    }
}

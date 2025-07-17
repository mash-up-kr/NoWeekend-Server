package noweekend.storage.db.core.weekend

import noweekend.core.domain.weekend.Weekend
import noweekend.core.domain.weekend.WeekendRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class WeekendCoreRepository(
    private val jpaRepository: WeekendJpaRepository,
) : WeekendRepository {
    override fun register(weekend: Weekend) {
        jpaRepository.save(weekend.toEntity())
    }

    override fun findByYear(year: Int): List<Weekend> {
        val start = LocalDate.of(year, 1, 1)
        val end = LocalDate.of(year, 12, 31)
        return jpaRepository.findAllByDateBetween(start, end).map { it.toDomain() }
    }

    override fun findAfterDate(date: LocalDate): List<Weekend> {
        return jpaRepository.findAllByDateAfter(date).map { it.toDomain() }
    }
}

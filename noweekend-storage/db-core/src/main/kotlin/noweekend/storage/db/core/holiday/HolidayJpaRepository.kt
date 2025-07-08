package noweekend.storage.db.core.holiday

import org.springframework.data.jpa.repository.JpaRepository

interface HolidayJpaRepository : JpaRepository<HolidayEntity, String> {
    fun findAllByYear(year: Int): List<HolidayEntity>
}

package noweekend.storage.db.core.sandwich

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(
    name = "sandwich_recommend_metadata",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_search_date",
            columnNames = ["search_date"],
        ),
    ],
)
class SandwichRecommendCacheEntity(
    @Id
    val id: String,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDate,

    @Column(name = "end_date", nullable = false)
    val endDate: LocalDate,

    @Column(name = "search_date", nullable = false)
    val searchDate: LocalDateTime,

    @Column(name = "use_annual_leave", nullable = false)
    val useAnnualLeave: Int,

    @Column(name = "total_vacation_days", nullable = false)
    val totalVacationDays: Int,
)

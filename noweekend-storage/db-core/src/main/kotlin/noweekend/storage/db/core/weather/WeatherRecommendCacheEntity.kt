package noweekend.storage.db.core.weather

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import noweekend.storage.db.core.BaseEntity
import java.time.LocalDate

@Entity
@Table(
    name = "weather_recommend_metadata",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_lat_lon_date",
            columnNames = ["latitude", "longitude", "search_date"],
        ),
    ],
)
class WeatherRecommendCacheEntity(
    @Id
    val id: String,

    @Column(nullable = false)
    val latitude: Double,

    @Column(nullable = false)
    val longitude: Double,

    @Column(name = "search_date", nullable = false)
    val searchDate: LocalDate,

    @Column(name = "recommend_json", columnDefinition = "TEXT")
    val recommendJson: String,
) : BaseEntity()

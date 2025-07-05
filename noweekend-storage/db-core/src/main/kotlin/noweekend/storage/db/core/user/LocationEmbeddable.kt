package noweekend.storage.db.core.user

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import noweekend.core.domain.user.Location

@Embeddable
data class LocationEmbeddable(
    @Column(name = "latitude")
    val latitude: Double? = null,

    @Column(name = "longitude")
    val longitude: Double? = null,
) {
    fun toDomain(): Location? {
        val lat = latitude
        val lng = longitude
        return if (lat != null && lng != null) Location(lat, lng) else null
    }

    companion object {
        fun fromDomain(location: Location?): LocationEmbeddable? =
            location?.let { LocationEmbeddable(it.latitude, it.longitude) }
    }
}

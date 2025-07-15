package noweekend.core.domain.user

import kotlin.math.roundToInt

data class Location(
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        fun rounded(latitude: Double, longitude: Double): Location {
            val latRounded = (latitude * 10).roundToInt() / 10.0
            val lonRounded = (longitude * 10).roundToInt() / 10.0
            return Location(latRounded, lonRounded)
        }
    }
}

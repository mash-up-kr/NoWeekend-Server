package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema

data class LocationRequest(
    @Schema(description = "위도", example = "37.5665")
    val latitude: Double,
    @Schema(description = "경도", example = "126.9780")
    val longitude: Double,
)

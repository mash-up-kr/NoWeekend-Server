package noweekend.mcpserver.domain

data class KmaForecastResponse(
    val response: KmaResponseBody,
)

data class KmaResponseBody(
    val body: KmaBodyData,
)

data class KmaBodyData(
    val items: KmaItems,
)

data class KmaItems(
    val item: List<KmaForecastItem>,
)

data class KmaForecastItem(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    val fcstDate: String,
    val fcstTime: String,
    val fcstValue: String,
    val nx: Int,
    val ny: Int,
)

data class LocationRequest(
    val latitude: Double,
    val longitude: Double,
)

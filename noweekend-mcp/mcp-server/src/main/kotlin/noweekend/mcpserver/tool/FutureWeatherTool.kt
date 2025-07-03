package noweekend.mcpserver.tool

import noweekend.mcpserver.client.KmaWeatherClient
import noweekend.mcpserver.domain.DayOfWeekKor
import noweekend.mcpserver.domain.DayWeatherSummary
import noweekend.mcpserver.domain.KmaForecastItem
import noweekend.mcpserver.domain.LocationRequest
import noweekend.mcpserver.domain.PrecipType
import noweekend.mcpserver.domain.RainSnowPeriod
import org.springframework.ai.tool.annotation.Tool
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

@Component
class FutureWeatherTool(
    private val kmaWeatherClient: KmaWeatherClient,
    @Value("\${kma.api.key}") private val apiKey: String,
) {

    @Tool(
        name = "getRawKmaForecast",
        description = WEATHER_TOOL_DESCRIPTION,
    )
    fun getRainSnowSummaryByDay(req: LocationRequest): List<DayWeatherSummary> {
        val datesToCheck = getTargetDates()
        val (nx, ny) = convertLatLngToGrid(req.latitude, req.longitude)
        val (baseDate, baseTime) = getLatestBaseDateTime()
        val allItems = fetchForecastItems(nx, ny, baseDate, baseTime)
        return summarizeByDate(datesToCheck, allItems)
    }

    private fun getTargetDates(): List<String> {
        val today = LocalDateTime.now()
        val daysUntilSunday = DayOfWeek.SUNDAY.value - today.dayOfWeek.value
        val dates = (0..daysUntilSunday).map {
            today.plusDays(it.toLong()).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        }
        return dates.take(3)
    }

    private fun fetchForecastItems(nx: Int, ny: Int, baseDate: String, baseTime: String): List<KmaForecastItem> {
        return kmaWeatherClient.getForecast(
            serviceKey = apiKey,
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny,
        ).response.body.items.item
    }

    private fun summarizeByDate(
        datesToCheck: List<String>,
        allItems: List<KmaForecastItem>,
    ): List<DayWeatherSummary> {
        val itemsByDate = allItems
            .filter { it.fcstDate in datesToCheck }
            .groupBy { it.fcstDate }

        return datesToCheck.map { date ->
            val localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"))
            val dayOfWeekKor = DayOfWeekKor.fromDayOfWeek(localDate.dayOfWeek)
            val dailyItems = itemsByDate[date].orEmpty()
            // rainSnowPeriods로 반환
            val rainSnowPeriods = extractRainSnowPeriods(dailyItems)
            DayWeatherSummary(date, dayOfWeekKor, rainSnowPeriods)
        }
    }

    private fun extractRainSnowPeriods(dailyItems: List<KmaForecastItem>): List<RainSnowPeriod> {
        val timeTypeList = dailyItems
            .groupBy { it.fcstTime }
            .mapNotNull { (time, itemsAtTime) ->
                if (!isWorkTime(time)) return@mapNotNull null
                val pty = itemsAtTime.find { it.category == "PTY" }
                val pcp = itemsAtTime.find { it.category == "PCP" }
                val sno = itemsAtTime.find { it.category == "SNO" }
                val pop = itemsAtTime.find { it.category == "POP" }

                when {
                    pty != null && pty.fcstValue != "0" -> time to when (pty.fcstValue) {
                        "1", "4" -> PrecipType.RAIN
                        "2" -> PrecipType.RAIN_AND_SNOW
                        "3" -> PrecipType.SNOW
                        else -> PrecipType.UNKNOWN
                    }

                    pcp != null && pcp.fcstValue != "강수없음" && pcp.fcstValue != "0" -> time to PrecipType.RAIN
                    sno != null && sno.fcstValue != "적설없음" && sno.fcstValue != "0" -> time to PrecipType.SNOW
                    pop != null && pop.fcstValue.toIntOrNull()
                        ?.let { it >= 60 } == true -> time to PrecipType.POSSIBLE_RAIN

                    else -> null
                }
            }
            .sortedBy { it.first }

        if (timeTypeList.isEmpty()) return emptyList()

        val result = mutableListOf<RainSnowPeriod>()
        var curType = timeTypeList.first().second
        var curStart = timeTypeList.first().first
        var curEnd = curStart

        for ((time, type) in timeTypeList.drop(1)) {
            // 시간 연속 + 타입 같으면 구간 연장
            if (type == curType && time.toInt() == curEnd.toInt() + 100) {
                curEnd = time
            } else {
                result.add(RainSnowPeriod(curType, curStart, curEnd))
                curType = type
                curStart = time
                curEnd = time
            }
        }
        result.add(RainSnowPeriod(curType, curStart, curEnd)) // 마지막 구간

        return result
    }

    private fun isWorkTime(timeStr: String): Boolean {
        val hour = timeStr.take(2).toIntOrNull() ?: return false
        return hour in 7..20
    }

    private fun getLatestBaseDateTime(): Pair<String, String> {
        val baseTimes = listOf("2300", "2000", "1700", "1400", "1100", "0800", "0500", "0200")
        val now = LocalDateTime.now()
        val currentHourMinute = now.format(DateTimeFormatter.ofPattern("HHmm"))
        val today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        for (bt in baseTimes) {
            if (currentHourMinute >= bt) {
                return today to bt
            }
        }
        val yesterday = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        return yesterday to "2300"
    }

    private fun convertLatLngToGrid(latitude: Double, longitude: Double): Pair<Int, Int> {
        val earthRadius = 6371.00877
        val gridSpacing = 5.0
        val projectionLat1 = 30.0
        val projectionLat2 = 60.0
        val baseLongitude = 126.0
        val baseLatitude = 38.0
        val baseX = 43
        val baseY = 136
        val degToRad = Math.PI / 180.0
        val re = earthRadius / gridSpacing
        val slat1 = projectionLat1 * degToRad
        val slat2 = projectionLat2 * degToRad
        val olon = baseLongitude * degToRad
        val olat = baseLatitude * degToRad
        val sn = ln(cos(slat1) / cos(slat2)) /
            ln(tan(Math.PI * 0.25 + slat2 * 0.5) / tan(Math.PI * 0.25 + slat1 * 0.5))
        val sf = tan(Math.PI * 0.25 + slat1 * 0.5)
            .pow(sn) * cos(slat1) / sn
        val ro = re * sf / tan(Math.PI * 0.25 + olat * 0.5).pow(sn)
        val ra = re * sf / tan(Math.PI * 0.25 + latitude * degToRad * 0.5).pow(sn)
        var theta = longitude * degToRad - olon
        if (theta > Math.PI) theta -= 2.0 * Math.PI
        if (theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn
        val nx = (ra * sin(theta) + baseX + 0.5).toInt()
        val ny = (ro - ra * cos(theta) + baseY + 0.5).toInt()
        return nx to ny
    }

    companion object {
        private const val WEATHER_TOOL_DESCRIPTION = """
        This tool returns summarized periods of rain/snow/possible rain for each day (up to 3 days) at a given location.
        
        - Only data measured between 07:00 and 20:00 (inclusive) is considered. Data outside this range is filtered out.
        - If the same precipitation type (rain, snow, rain and snow, possible rain) is detected for consecutive time slots (e.g. 0800, 0900, 1000), those slots are **merged as a single period** (startTime~endTime).
        - Each day's result is a list of 'RainSnowPeriod', where each period has: 
            - `type`: PrecipType (RAIN, SNOW, RAIN_AND_SNOW, POSSIBLE_RAIN, UNKNOWN)
            - `startTime`/`endTime`: time string (e.g. "0800", "1300"), denoting the start and end of that precipitation event.
        - For each day, if there is no relevant event during work hours, an empty list is returned.
        - Periods with only 'possible rain' (강수확률 60% 이상) are included as type 'POSSIBLE_RAIN' when there's no other certain rain/snow info.
        
        Example return:
        [
            {
                "date": "20250703",
                "dayOfWeek": "THU",
                "rainSnowInfos": [
                    {"type": "RAIN", "startTime": "0900", "endTime": "1300"},
                    {"type": "POSSIBLE_RAIN", "startTime": "1500", "endTime": "1700"},
                    {"type": "SNOW", "startTime": "1800", "endTime": "1900"}
                ]
            },
            {
                "date": "20250704",
                "dayOfWeek": "FRI",
                "rainSnowInfos": []
            }
        ]
        - `date` is formatted as "yyyyMMdd"
        - `dayOfWeek` is the abbreviated English weekday (THU, FRI, etc), or can be a custom enum.
        - `rainSnowInfos` is a list of periods; if empty, there is no rain/snow/possible rain during work hours.
        
        PrecipType values:
        - RAIN: Confirmed rain
        - SNOW: Confirmed snow
        - RAIN_AND_SNOW: Mixed rain and snow
        - POSSIBLE_RAIN: Only probability (POP>=60) detected, no certain rain/snow
        - UNKNOWN: Rare/unsupported types

        Use-case: This tool is designed for concise rain/snow summary, not detailed every-hour forecast.
    """
    }
}

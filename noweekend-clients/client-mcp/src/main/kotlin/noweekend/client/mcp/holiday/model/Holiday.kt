package noweekend.client.mcp.holiday.model

import noweekend.core.domain.holiday.DayOfWeekKor
import noweekend.core.domain.holiday.Holiday
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HolidayItemDto(
    val dateName: String,
    val localDate: Int,
    val seq: Int,
)

data class HolidayItemsDto(
    val item: List<HolidayItemDto>?,
)

data class HolidayBodyDto(
    val items: HolidayItemsDto?,
)

data class HolidayResponseDto(
    val body: HolidayBodyDto?,
)

data class HolidayRootDto(
    val response: HolidayResponseDto?,
)

data class HolidayRequest(
    val year: Int,
    val month: Int,
)

fun HolidayItemDto.toDomain(): Holiday {
    val dateStr = localDate.toString()
    val parsedDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"))
    return Holiday.register(
        year = parsedDate.year,
        month = parsedDate.monthValue,
        content = dateName,
        dayOfWeekKor = parsedDate.dayOfWeek.toKorEnum(),
    )
}

fun DayOfWeek.toKorEnum(): DayOfWeekKor = when (this) {
    DayOfWeek.MONDAY -> DayOfWeekKor.MON
    DayOfWeek.TUESDAY -> DayOfWeekKor.TUE
    DayOfWeek.WEDNESDAY -> DayOfWeekKor.WED
    DayOfWeek.THURSDAY -> DayOfWeekKor.THU
    DayOfWeek.FRIDAY -> DayOfWeekKor.FRI
    DayOfWeek.SATURDAY -> DayOfWeekKor.SAT
    DayOfWeek.SUNDAY -> DayOfWeekKor.SUN
}

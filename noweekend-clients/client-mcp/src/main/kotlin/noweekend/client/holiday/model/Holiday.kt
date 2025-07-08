package noweekend.client.holiday.model

import noweekend.core.domain.holiday.DayOfWeekKor
import noweekend.core.domain.holiday.Holiday
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HolidayItemDto(
    val dateName: String,
    val localDate: Int,
    val seq: Int
)

data class HolidayItemsDto(
    val item: List<HolidayItemDto>?
)

data class HolidayBodyDto(
    val items: HolidayItemsDto?
)

data class HolidayResponseDto(
    val body: HolidayBodyDto?
)

data class HolidayRootDto(
    val response: HolidayResponseDto?
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
        dayOfWeekKor = parsedDate.dayOfWeek.toKorEnum()
    )
}

fun java.time.DayOfWeek.toKorEnum(): DayOfWeekKor = when(this) {
    java.time.DayOfWeek.MONDAY -> DayOfWeekKor.MON
    java.time.DayOfWeek.TUESDAY -> DayOfWeekKor.TUE
    java.time.DayOfWeek.WEDNESDAY -> DayOfWeekKor.WED
    java.time.DayOfWeek.THURSDAY -> DayOfWeekKor.THU
    java.time.DayOfWeek.FRIDAY -> DayOfWeekKor.FRI
    java.time.DayOfWeek.SATURDAY -> DayOfWeekKor.SAT
    java.time.DayOfWeek.SUNDAY -> DayOfWeekKor.SUN
}
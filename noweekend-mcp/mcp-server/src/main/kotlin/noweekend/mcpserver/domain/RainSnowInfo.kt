package noweekend.mcpserver.domain

enum class PrecipType {
    RAIN,
    SNOW,
    RAIN_AND_SNOW,
    POSSIBLE_RAIN,
    UNKNOWN,
}

enum class DayOfWeekKor(val display: String) {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일"),
    ;

    companion object {
        fun fromDayOfWeek(dow: java.time.DayOfWeek): DayOfWeekKor =
            when (dow) {
                java.time.DayOfWeek.MONDAY -> MON
                java.time.DayOfWeek.TUESDAY -> TUE
                java.time.DayOfWeek.WEDNESDAY -> WED
                java.time.DayOfWeek.THURSDAY -> THU
                java.time.DayOfWeek.FRIDAY -> FRI
                java.time.DayOfWeek.SATURDAY -> SAT
                java.time.DayOfWeek.SUNDAY -> SUN
            }
    }
}

data class RainSnowPeriod(
    val type: PrecipType,
    val startTime: String,
    val endTime: String,
)

data class DayWeatherSummary(
    // ex) "20240703"
    val date: String,
    val dayOfWeek: DayOfWeekKor,
    val rainSnowInfos: List<RainSnowPeriod>,
)

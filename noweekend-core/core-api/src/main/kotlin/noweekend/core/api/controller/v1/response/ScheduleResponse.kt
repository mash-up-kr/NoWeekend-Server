package noweekend.core.api.controller.v1.response

import io.swagger.v3.oas.annotations.media.Schema
import noweekend.core.domain.enumerate.AlarmOption
import noweekend.core.domain.enumerate.ScheduleCategory
import java.time.LocalDate
import java.time.LocalDateTime

@Schema(description = "단일 일정 응답")
data class ScheduleResponse(
    @Schema(description = "일정 ID")
    val id: String,

    @Schema(description = "제목")
    val title: String,

    @Schema(description = "시작 시간")
    val startTime: LocalDateTime,

    @Schema(description = "종료 시간")
    val endTime: LocalDateTime,

    @Schema(description = "카테고리")
    val category: ScheduleCategory,

    @Schema(description = "온도")
    val temperature: Int,

    @Schema(description = "하루종일 여부")
    val allDay: Boolean,

    @Schema(description = "알람 옵션")
    val alarmOption: AlarmOption,

    @Schema(description = "완료 여부")
    val completed: Boolean,
)

@Schema(description = "하루 스케줄 응답")
data class DailyScheduleResponse(
    @Schema(description = "날짜")
    val date: LocalDate,

    @Schema(description = "해당 일의 평균 온도")
    val dailyTemperature: Int,

    @Schema(description = "일정 리스트")
    val schedules: List<ScheduleResponse>,
)

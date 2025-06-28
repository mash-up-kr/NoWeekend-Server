package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import noweekend.core.domain.enumerate.AlarmOption
import noweekend.core.domain.enumerate.ScheduleCategory
import java.time.LocalDate
import java.time.LocalTime

@Schema(description = "일정 생성 요청")
data class ScheduleCreateRequest(
    @field:NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "제목")
    val title: String,

    @field:NotNull(message = "날짜는 필수입니다.")
    @Schema(description = "날짜")
    val date: LocalDate,

    @Schema(description = "시작 시간")
    val startTime: LocalTime? = null,

    @Schema(description = "종료 시간")
    val endTime: LocalTime? = null,

    @field:NotBlank(message = "카테고리는 필수입니다.")
    @Schema(description = "카테고리")
    val category: ScheduleCategory,

    @field:NotNull(message = "온도는 필수입니다.")
    @field:Positive(message = "온도는 양수여야 합니다.")
    @field:Max(value = 100, message = "온도는 100 이하여야 합니다.")
    @Schema(description = "온도 (감정 등 표현)")
    val temperature: Int,

    @field:NotNull(message = "하루종일 여부는 필수입니다.")
    @Schema(description = "하루종일 여부")
    val allDay: Boolean,

    @field:NotNull(message = "알람 설정은 필수입니다.")
    @Schema(description = "알람 설정")
    val alarmOption: AlarmOption,
)

@Schema(description = "일정 수정 요청")
data class ScheduleUpdateRequest(
    @field:NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "제목")
    val title: String,

    @field:NotNull(message = "시작 시간은 필수입니다.")
    @Schema(description = "시작 시간")
    val startTime: LocalTime,

    @field:NotNull(message = "종료 시간은 필수입니다.")
    @Schema(description = "종료 시간")
    val endTime: LocalTime,

    @field:NotBlank(message = "카테고리는 필수입니다.")
    @Schema(description = "카테고리")
    val category: ScheduleCategory,

    @field:NotNull(message = "온도는 필수입니다.")
    @field:Positive(message = "온도는 양수여야 합니다.")
    @field:Max(value = 100, message = "온도는 100 이하여야 합니다.")
    @Schema(description = "온도 (감정 등 표현)")
    val temperature: Int,

    @field:NotNull(message = "하루종일 여부는 필수입니다.")
    @Schema(description = "하루종일 여부")
    val allDay: Boolean,

    @field:NotNull(message = "알람 설정은 필수입니다.")
    @Schema(description = "알람 설정")
    val alarmOption: AlarmOption,
)

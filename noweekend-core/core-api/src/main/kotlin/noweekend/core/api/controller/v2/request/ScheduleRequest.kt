package noweekend.core.api.controller.v2.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import noweekend.core.domain.enumerate.AlarmOption
import noweekend.core.domain.enumerate.ScheduleCategory
import java.time.LocalDateTime

@Schema(description = "일정 생성 요청")
data class ScheduleCreateRequestV2(
    @field:NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "제목")
    val title: String,

    @field:NotNull(message = "시작 시간은 필수입니다.")
    @Schema(description = "시작 시간")
    val startDateTime: LocalDateTime,

    @field:NotNull(message = "종료 시간은 필수입니다.")
    @Schema(description = "종료 시간")
    val endDateTime: LocalDateTime,

    @field:NotNull(message = "카테고리는 필수입니다.")
    @Schema(description = "카테고리")
    val category: ScheduleCategory,

    @field:NotNull(message = "온도는 필수입니다.")
    @field:Positive(message = "온도는 양수여야 합니다.")
    @field:Max(value = 100, message = "온도는 100 이하여야 합니다.")
    @Schema(description = "온도 (감정 등 표현)")
    val temperature: Int,

    @field:NotNull(message = "알람 설정은 필수입니다.")
    @Schema(description = "알람 설정")
    val alarmOption: AlarmOption,
)

@Schema(description = "일정 수정 요청")
data class ScheduleUpdateRequestV2(
    @field:NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "제목")
    val title: String,

    @field:NotNull(message = "시작 시간은 필수입니다.")
    @Schema(description = "시작 시간")
    val startDateTime: LocalDateTime,

    @field:NotNull(message = "종료 시간은 필수입니다.")
    @Schema(description = "종료 시간")
    val endDateTime: LocalDateTime,

    @field:NotNull(message = "카테고리는 필수입니다.")
    @Schema(description = "카테고리")
    val category: ScheduleCategory,

    @field:NotNull(message = "온도는 필수입니다.")
    @field:Positive(message = "온도는 양수여야 합니다.")
    @field:Max(value = 100, message = "온도는 100 이하여야 합니다.")
    @Schema(description = "온도 (감정 등 표현)")
    val temperature: Int,

    @field:NotNull(message = "알람 설정은 필수입니다.")
    @Schema(description = "알람 설정")
    val alarmOption: AlarmOption,
)

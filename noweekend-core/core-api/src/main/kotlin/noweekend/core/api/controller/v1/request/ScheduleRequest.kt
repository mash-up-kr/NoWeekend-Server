package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import noweekend.core.domain.enumerate.AlarmOption
import noweekend.core.domain.schedule.BasicTag
import java.time.LocalTime

@Schema(description = "자주하는 일정 DTO")
data class TagRequest(
    @field:Schema(
        description = "자주하는 일정 태그(한글) 목록, 예시: [\"회의 참석\", \"헬스장 운동\", \"스터디\"]",
        example = "[\"회의 참석\", \"헬스장 운동\", \"스터디\"]",
    )
    val scheduleTags: List<String>,
) {
    fun validatedScheduleTags(): List<BasicTag> {
        val result = mutableListOf<BasicTag>()
        val invalid = mutableListOf<String>()

        scheduleTags.forEach {
            try {
                result.add(BasicTag.fromKorean(it.trim()))
            } catch (e: NoSuchElementException) {
                invalid.add(it)
            }
        }
        if (invalid.isNotEmpty()) throw IllegalArgumentException("유효하지 않은 태그입니다: $invalid")
        return result
    }
}

@Schema(description = "일정 생성 요청")
data class ScheduleCreateRequest(
    @field:NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "제목")
    val title: String,

    @Schema(description = "시작 시간")
    val startTime: LocalTime? = null,

    @Schema(description = "종료 시간")
    val endTime: LocalTime? = null,

    @field:NotBlank(message = "카테고리는 필수입니다.")
    @Schema(description = "카테고리")
    val category: String,

    @field:NotNull(message = "온도는 필수입니다.")
    @Schema(description = "온도 (감정 등 표현) -1 ~ 5 등급")
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
    @field:NotNull(message = "시작 시간은 필수입니다.")
    @Schema(description = "시작 시간")
    val startTime: LocalTime,

    @field:NotNull(message = "종료 시간은 필수입니다.")
    @Schema(description = "종료 시간")
    val endTime: LocalTime,

    @field:NotBlank(message = "카테고리는 필수입니다.")
    @Schema(description = "카테고리")
    val category: String,

    @field:NotNull(message = "온도는 필수입니다.")
    @Schema(description = "온도 (감정 등 표현) -1 ~ 5 등급")
    val temperature: Int,

    @field:NotNull(message = "하루종일 여부는 필수입니다.")
    @Schema(description = "하루종일 여부")
    val allDay: Boolean,

    @field:NotNull(message = "알람 설정은 필수입니다.")
    @Schema(description = "알람 설정")
    val alarmOption: AlarmOption,
)

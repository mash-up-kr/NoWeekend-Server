package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import noweekend.core.domain.schedule.ScheduleTag

@Schema(description = "자주하는 일정 DTO")
data class ScheduleRequest(
    @field:NotBlank(message = "authorizationCode는 필수입니다.")
    @field:Schema(
        description = "자주하는 일정 태그(영문) 목록, 예시: [\"COMMUTE\", \"LEAVE\", \"GYM\"]",
        example = "[\"COMMUTE\", \"LEAVE\", \"GYM\"]",
    )
    val scheduleTags: List<String>,
) {
    fun validatedScheduleTags(): List<ScheduleTag> {
        val result = mutableListOf<ScheduleTag>()
        val invalid = mutableListOf<String>()

        scheduleTags.forEach {
            try {
                result.add(ScheduleTag.valueOf(it.trim().uppercase()))
            } catch (e: Exception) {
                invalid.add(it)
            }
        }
        if (invalid.isNotEmpty()) throw IllegalArgumentException("유효하지 않은 태그입니다: $invalid")
        return result
    }
}

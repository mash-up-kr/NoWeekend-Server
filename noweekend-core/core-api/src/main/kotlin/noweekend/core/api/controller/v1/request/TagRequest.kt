package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import noweekend.core.domain.schedule.BasicTag

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

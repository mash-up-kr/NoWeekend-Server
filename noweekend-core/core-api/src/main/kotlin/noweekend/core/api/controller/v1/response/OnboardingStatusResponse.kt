package noweekend.core.api.controller.v1.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "온보딩 진행 상태 응답")
data class OnboardingStatusResponse(
    @Schema(
        description = "온보딩 단계 상태 (NONE: 이름/생년월일 미입력, NAME_AND_BIRTHDAY: 이름/생년월일만 입력, ANNUAL_LEAVE: 연차까지 입력, DONE: 모든 온보딩 완료)",
        example = "ANNUAL_LEAVE",
    )
    val status: OnboardingStatus,
)

@Schema(description = "온보딩 상태 Enum")
enum class OnboardingStatus {
    @Schema(description = "이름/생년월일 입력 전 상태")
    NONE,

    @Schema(description = "이름/생년월일 입력 완료 상태")
    NAME_AND_BIRTHDAY,

    @Schema(description = "연차 입력까지 완료된 상태")
    ANNUAL_LEAVE,

    @Schema(description = "모든 온보딩 완료 상태")
    DONE,
}

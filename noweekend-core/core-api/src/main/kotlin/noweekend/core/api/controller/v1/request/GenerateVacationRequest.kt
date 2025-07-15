package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import noweekend.core.domain.ActivityType
import noweekend.core.domain.LeisurePreference
import noweekend.core.domain.RestPreference
import noweekend.core.domain.TravelStyle

@Schema(description = "휴가 추천을 위한 사용자 입력 정보")
data class GenerateVacationRequest(
    @field:NotNull(message = "사용할 연차 일수는 필수입니다.")
    @field:Min(value = 1, message = "최소 1일 이상 입력해주세요.")
    @field:Max(value = 15, message = "최대 15일 이하로 입력해주세요.")
    @Schema(description = "사용할 연차 일수 (1~15)")
    val days: Int,

    @field:NotNull(message = "여행 스타일 선택은 필수입니다.")
    @Schema(description = "여행 스타일")
    val travelStyle: TravelStyle,

    @field:NotNull(message = "활동 유형 선택은 필수입니다.")
    @Schema(description = "활동 유형")
    val activityType: ActivityType,

    @field:NotNull(message = "휴식 유형 선택은 필수입니다.")
    @Schema(description = "휴식 유형")
    val restPreference: RestPreference,

    @field:NotNull(message = "관심사 선택은 필수입니다.")
    @Schema(description = "관심사")
    val leisurePreference: LeisurePreference,

)

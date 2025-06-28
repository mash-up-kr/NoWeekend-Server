package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

@Schema(description = "올해 연차 입력 DTO")
data class LeaveInputRequest(
    @field:NotNull(message = "연차 일수는 필수입니다.")
    @field:Min(0, message = "연차 일수는 0 이상이어야 합니다.")
    @field:Max(100, message = "연차 일수는 100 이하여야 합니다.")
    @Schema(description = "남은 연차 일수", example = "15")
    val days: Int,

    @field:ValidLeaveHours
    val hours: Int,
)

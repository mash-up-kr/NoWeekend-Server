package noweekend.core.api.controller.v1

import noweekend.core.api.controller.v1.request.ScheduleCreateRequest
import noweekend.core.api.controller.v1.request.ScheduleUpdateRequest
import noweekend.core.api.controller.v1.response.DailyScheduleResponse
import noweekend.core.api.controller.v1.response.ScheduleResponse
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/schedule")
class ScheduleController : ScheduleControllerDocs {

    @GetMapping
    override fun getSchedules(
        @CurrentUserId userId: String,
        @RequestParam(name = "start_date", required = true) startDate: LocalDate,
        @RequestParam(name = "end_date", required = true) endDate: LocalDate,
    ): ApiResponse<List<DailyScheduleResponse>> {
        return ApiResponse.success(listOf(1, 2, 3).map { DailyScheduleResponse.sample() })
    }

    @PostMapping
    override fun createSchedule(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: ScheduleCreateRequest,
    ): ApiResponse<ScheduleResponse> {
        return ApiResponse.success(ScheduleResponse.sample())
    }

    @PutMapping("/{id}")
    override fun updateSchedule(
        @CurrentUserId userId: String,
        @PathVariable id: String,
        @Validated @RequestBody request: ScheduleUpdateRequest,
    ): ApiResponse<ScheduleResponse> {
        return ApiResponse.success(ScheduleResponse.sample())
    }

    @DeleteMapping("/{id}")
    override fun deleteSchedule(@CurrentUserId userId: String, @PathVariable id: String): ApiResponse<String> {
        return ApiResponse.success(id)
    }
}

package noweekend.core.api.controller.v1

import noweekend.core.api.controller.v1.request.ScheduleCreateRequest
import noweekend.core.api.controller.v1.request.ScheduleUpdateRequest
import noweekend.core.api.controller.v1.response.DailyScheduleResponse
import noweekend.core.api.controller.v1.response.ScheduleResponse
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.api.service.schedule.ScheduleApplicationService
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
class ScheduleController(
    private val scheduleApplicationService: ScheduleApplicationService,
) : ScheduleControllerDocs {

    @GetMapping
    override fun getSchedules(
        @CurrentUserId userId: String,
        @RequestParam(name = "start_date", required = true) startDate: LocalDate,
        @RequestParam(name = "end_date", required = true) endDate: LocalDate,
    ): ApiResponse<List<DailyScheduleResponse>> {
        val schedules = scheduleApplicationService.getSchedulesByDateRange(userId, startDate, endDate)
        return ApiResponse.success(schedules)
    }

    @PostMapping
    override fun createSchedule(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: ScheduleCreateRequest,
    ): ApiResponse<ScheduleResponse> {
        val schedule = scheduleApplicationService.createSchedule(userId, request)
        return ApiResponse.success(schedule)
    }

    @PutMapping("/{id}")
    override fun updateSchedule(
        @CurrentUserId userId: String,
        @PathVariable id: String,
        @Validated @RequestBody request: ScheduleUpdateRequest,
    ): ApiResponse<ScheduleResponse> {
        val schedule = scheduleApplicationService.updateSchedule(userId, id, request)
        return ApiResponse.success(schedule)
    }

    @DeleteMapping("/{id}")
    override fun deleteSchedule(@CurrentUserId userId: String, @PathVariable id: String): ApiResponse<String> {
        scheduleApplicationService.deleteSchedule(userId, id)
        return ApiResponse.success("Schedule deleted successfully")
    }
}

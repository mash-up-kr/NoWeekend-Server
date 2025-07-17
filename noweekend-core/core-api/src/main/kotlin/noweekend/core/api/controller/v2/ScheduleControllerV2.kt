package noweekend.core.api.controller.v2

import noweekend.core.api.controller.v1.response.ScheduleResponse
import noweekend.core.api.controller.v2.docs.ScheduleControllerDocsV2
import noweekend.core.api.controller.v2.request.ScheduleCreateRequestV2
import noweekend.core.api.controller.v2.request.ScheduleUpdateRequestV2
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.api.service.schedule.ScheduleApplicationService
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/schedule")
class ScheduleControllerV2(
    private val scheduleApplicationService: ScheduleApplicationService,
) : ScheduleControllerDocsV2 {

    @PostMapping
    override fun createSchedule(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: ScheduleCreateRequestV2,
    ): ApiResponse<ScheduleResponse> {
        return ApiResponse.success(scheduleApplicationService.createScheduleV2(userId, request))
    }

    @PutMapping("/{id}")
    override fun updateSchedule(
        @CurrentUserId userId: String,
        @PathVariable id: String,
        @Validated @RequestBody request: ScheduleUpdateRequestV2,
    ): ApiResponse<ScheduleResponse> {
        return ApiResponse.success(scheduleApplicationService.updateScheduleV2(userId, id, request))
    }
}

package noweekend.core.api.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import noweekend.core.api.controller.v1.request.ScheduleCreateRequest
import noweekend.core.api.controller.v1.request.ScheduleUpdateRequest
import noweekend.core.api.controller.v1.response.DailyScheduleResponse
import noweekend.core.api.controller.v1.response.ScheduleResponse
import noweekend.core.support.response.ApiResponse
import java.time.LocalDate
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

interface ScheduleControllerDocs {

    @Operation(
        summary = "캘린더: 일정 조회",
        description = "startDate ~ endDate 구간에 포함된 DailySchedule 리스트를 반환합니다.",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "일정 조회 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "SUCCESS",
  "data": [
    {
      "date": "2025-05-01",
      "schedules": [
        {
          "id": "abc123",
          "title": "회의",
          "startTime": "2025-05-01T10:00:00",
          "endTime": "2025-05-01T11:00:00",
          "category": "COMPANY",
          "temperature": 3,
          "allDay": false,
          "alarmOption": "FIFTEEN_MINUTES_BEFORE",
          "completed": false
        }
      ]
    }
  ],
  "error": null
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "올바르지 않은 요청입니다.",
    "data": {}
  }
}
""",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun getSchedules(
        @Schema(hidden = true) userId: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): ApiResponse<List<DailyScheduleResponse>>

    @Operation(
        summary = "캘린더: 일정 생성",
        description = "일정을 생성합니다.",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ScheduleCreateRequest::class),
                    examples = [
                        ExampleObject(
                            name = "예시 요청",
                            value = """
{
  "title": "회의",
  "date": "2025-05-01",
  "startTime": "10:00:00",
  "endTime": "11:00:00",
  "category": "COMPANY",
  "temperature": 3,
  "allDay": false,
  "alarmOption": "FIFTEEN_MINUTES_BEFORE"
}
""",
                        ),
                    ],
                ),
            ],
        ),
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "일정 생성 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "SUCCESS",
  "data": {
    "id": "abc123",
    "title": "회의",
    "startTime": "2025-05-01T10:00:00",
    "endTime": "2025-05-01T11:00:00",
    "category": "COMPANY",
    "temperature": 3,
    "allDay": false,
    "alarmOption": "FIFTEEN_MINUTES_BEFORE",
    "completed": false
  },
  "error": null
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "올바르지 않은 요청입니다.",
    "data": {}
  }
}
""",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun createSchedule(
        @Schema(hidden = true) userId: String,
        request: ScheduleCreateRequest,
    ): ApiResponse<ScheduleResponse>

    @Operation(
        summary = "캘린더: 일정 수정",
        description = "일정의 시작/종료 시간, 카테고리, 온도, 하루종일 여부, 알람 옵션을 수정합니다.",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ScheduleUpdateRequest::class),
                ),
            ],
        ),
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "일정 수정 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "SUCCESS",
  "data": {
    "id": "abc123",
    "title": "회의",
    "startTime": "2025-05-01T10:00:00",
    "endTime": "2025-05-01T11:00:00",
    "category": "COMPANY",
    "temperature": 3,
    "allDay": false,
    "alarmOption": "FIFTEEN_MINUTES_BEFORE",
    "completed": false
  },
  "error": null
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "올바르지 않은 요청입니다.",
    "data": {}
  }
}
""",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun updateSchedule(
        @Schema(hidden = true) userId: String,
        id: String,
        request: ScheduleUpdateRequest,
    ): ApiResponse<ScheduleResponse>

    @Operation(
        summary = "캘린더: 일정 삭제",
        description = "일정을 삭제합니다.",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "일정 삭제 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "SUCCESS",
  "data": "일정이 성공적으로 삭제되었습니다.",
  "error": null
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "올바르지 않은 요청입니다.",
    "data": {}
  }
}
""",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun deleteSchedule(
        @Schema(hidden = true) userId: String,
        id: String,
    ): ApiResponse<String>
}

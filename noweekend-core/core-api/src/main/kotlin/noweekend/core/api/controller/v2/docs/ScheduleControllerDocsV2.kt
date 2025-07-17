package noweekend.core.api.controller.v2.docs

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import noweekend.core.api.controller.v1.response.ScheduleResponse
import noweekend.core.api.controller.v2.request.ScheduleCreateRequestV2
import noweekend.core.api.controller.v2.request.ScheduleUpdateRequestV2
import noweekend.core.support.response.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

interface ScheduleControllerDocsV2 {

    @Operation(
        summary = "캘린더: 일정 생성",
        description = "일정을 생성합니다.",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ScheduleCreateRequestV2::class),
                    examples = [
                        ExampleObject(
                            name = "예시 요청",
                            value = """
{
  "title": "회의",
  "startDateTime": "2024-05-27 10:00:00",
  "endDateTime": "2024-05-27 11:00:00",
  "category": "COMPANY",
  "temperature": 3,
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
    "startDateTime": "2025-05-01T10:00:00",
    "endDateTime": "2025-05-01T11:00:00",
    "category": "COMPANY",
    "temperature": 3,
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
        request: ScheduleCreateRequestV2,
    ): ApiResponse<ScheduleResponse>

    @Operation(
        summary = "캘린더: 일정 수정",
        description = "일정의 시작/종료 시간, 카테고리, 온도, 알람 옵션을 수정합니다.",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ScheduleUpdateRequestV2::class),
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
    "startDateTime": "2025-05-01T10:00:00",
    "endDateTime": "2025-05-01T11:00:00",
    "category": "COMPANY",
    "temperature": 3,
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
        request: ScheduleUpdateRequestV2,
    ): ApiResponse<ScheduleResponse>
}

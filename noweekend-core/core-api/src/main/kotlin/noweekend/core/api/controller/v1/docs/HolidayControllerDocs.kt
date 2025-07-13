package noweekend.core.api.controller.v1.docs

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.core.api.controller.v1.response.HolidayResponses
import noweekend.core.support.response.ApiResponse

@Tag(name = "공휴일", description = "공휴일 관련 API")
interface HolidayControllerDocs {

    @Operation(
        summary = "이번 달의 공휴일 목록 조회",
        description = """
현재 월(예: 2025-07)의 공휴일을 조회합니다.  
공휴일 데이터는 외부 API와 동기화된 DB에서 바로 조회되며,  
API 호출 시점에는 네트워크 호출이 발생하지 않습니다.
""",
        responses = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "이번 달 공휴일 목록 조회 성공",
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
    "holidays": [
      {
        "date": "2025-07-17",
        "content": "제헌절",
        "dayOfWeekKor": "목"
      }
    ]
  },
  "error": null
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
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
    fun getHolidays(): ApiResponse<HolidayResponses>

    @Operation(
        summary = "올해 남은 공휴일 조회",
        description = """
오늘(예: 2025-07-13) 이후(혹은 오늘 포함)의 공휴일을 모두 반환합니다.  
DB에서 미리 동기화된 데이터를 사용하며, 네트워크 호출은 없습니다.
""",
        responses = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "남은 공휴일 목록 조회 성공",
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
    "holidays": [
      {
        "date": "2025-07-17",
        "content": "제헌절",
        "dayOfWeekKor": "목"
      },
      {
        "date": "2025-08-15",
        "content": "광복절",
        "dayOfWeekKor": "금"
      },
      …
    ]
  },
  "error": null
}
""",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun getRemainingHolidays(): ApiResponse<HolidayResponses>
}

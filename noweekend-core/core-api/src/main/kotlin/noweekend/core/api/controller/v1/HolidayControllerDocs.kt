package noweekend.core.api.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.core.api.controller.v1.response.HolidayResponses
import noweekend.core.support.response.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@Tag(name = "공휴일", description = "공휴일 관련 API")
interface HolidayControllerDocs {

    @Operation(
        summary = "올해의 공휴일 목록 조회",
        description = "해당 연도의 모든 공휴일을 조회합니다. \n" +
            "최초 요청 시, 외부 공공데이터 API를 통해 1년치 공휴일 정보를 수집하여 DB에 저장하며, \n" +
            "이후에는 오늘 날짜 기준 최신화된 데이터가 있으면 바로 반환합니다.",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "공휴일 목록 조회 성공",
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
        "year": 2025,
        "month": 1,
        "content": "신정",
        "dayOfWeekKor": "WED"
      },
      {
        "year": 2025,
        "month": 2,
        "content": "설날",
        "dayOfWeekKor": "MON"
      },
      {
        "year": 2025,
        "month": 3,
        "content": "삼일절",
        "dayOfWeekKor": "SAT"
      }
      // ...
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
    fun getHolidays(): ApiResponse<HolidayResponses>
}

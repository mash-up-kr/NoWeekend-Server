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
        summary = "올해의 공휴일 목록 조회",
        description = "해당 연도의 모든 공휴일을 조회합니다. \n" +
            "최초 요청 시, 외부 공공데이터 API를 통해 1년치 공휴일 정보를 수집하여 DB에 저장하며, \n" +
            "이후에는 오늘 날짜 기준 최신화된 데이터가 있으면 바로 반환합니다.",
        responses = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
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
      {"year": 2025, "month": 1,  "day":  1, "content": "1월1일",                    "dayOfWeekKor": "WED"},
      {"year": 2025, "month": 1,  "day": 27, "content": "임시공휴일",               "dayOfWeekKor": "MON"},
      {"year": 2025, "month": 1,  "day": 28, "content": "설날",                 "dayOfWeekKor": "TUE"},
      {"year": 2025, "month": 1,  "day": 29, "content": "설날",                 "dayOfWeekKor": "WED"},
      {"year": 2025, "month": 1,  "day": 30, "content": "설날",                 "dayOfWeekKor": "THU"},
      {"year": 2025, "month": 3,  "day":  1, "content": "삼일절",               "dayOfWeekKor": "SAT"},
      {"year": 2025, "month": 3,  "day":  3, "content": "대체공휴일",            "dayOfWeekKor": "MON"},
      {"year": 2025, "month": 5,  "day":  5, "content": "부처님오신날",           "dayOfWeekKor": "MON"},
      {"year": 2025, "month": 5,  "day":  5, "content": "어린이날",             "dayOfWeekKor": "MON"},
      {"year": 2025, "month": 5,  "day":  6, "content": "대체공휴일",            "dayOfWeekKor": "TUE"},
      {"year": 2025, "month": 6,  "day":  3, "content": "임시공휴일(선거)",     "dayOfWeekKor": "TUE"},
      {"year": 2025, "month": 6,  "day":  6, "content": "현충일",               "dayOfWeekKor": "FRI"},
      {"year": 2025, "month": 8,  "day": 15, "content": "광복절",               "dayOfWeekKor": "FRI"},
      {"year": 2025, "month":10,  "day":  3, "content": "개천절",               "dayOfWeekKor": "FRI"},
      {"year": 2025, "month":10,  "day":  5, "content": "추석",                 "dayOfWeekKor": "SUN"},
      {"year": 2025, "month":10,  "day":  6, "content": "추석",                 "dayOfWeekKor": "MON"},
      {"year": 2025, "month":10,  "day":  7, "content": "추석",                 "dayOfWeekKor": "TUE"},
      {"year": 2025, "month":10,  "day":  8, "content": "대체공휴일",            "dayOfWeekKor": "WED"},
      {"year": 2025, "month":10,  "day":  9, "content": "한글날",               "dayOfWeekKor": "THU"},
      {"year": 2025, "month":12,  "day": 25, "content": "기독탄신일",           "dayOfWeekKor": "THU"}
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
}

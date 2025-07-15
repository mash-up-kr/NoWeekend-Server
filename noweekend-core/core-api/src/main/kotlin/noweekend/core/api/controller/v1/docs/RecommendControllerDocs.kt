package noweekend.core.api.controller.v1.docs

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.client.mcp.recommend.model.SandwichResponse
import noweekend.client.mcp.recommend.model.TagApiResponses
import noweekend.core.api.controller.v1.response.WeatherResponse
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.support.response.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@Tag(
    name = "추천",
    description = "MCP AI를 활용하여 다양한 정보를 추천합니다.",
)
interface RecommendControllerDocs {

    @Operation(
        summary = "날씨 기반 연차/반차 추천",
        description = """
            향후 3일 동안 오전 7시~오후 8시 사이에 눈 또는 비가 예보된 경우,
            연차 또는 반차 사용을 추천하는 메시지를 반환합니다.
        """,
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "추천 정보 반환 성공",
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
     "weatherResponses": [
            {
                "localDate": "2025-07-16",
                "recommendContent": "08시~09시, 11시~12시, 그리고 18시~20시까지 총 4시간 동안 비가 와요. 연차 어떠세요?"
            },
            {
                "localDate": "2025-07-17",
                "recommendContent": "07시~08시, 10시~17시까지 총 8시간 동안 비가 와요. 연차 추천드려요!"
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
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (ex. 로그인 정보 누락 등)",
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
    "message": "로그인 정보가 필요합니다.",
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
    fun getWeatherRecommend(
        @Parameter(hidden = true) @CurrentUserId userId: String,
    ): ApiResponse<WeatherResponse>

    @Operation(
        summary = "유저 태그 기반으로 선택했던 것 1개와 새로운 2개의 태그, 총 3개의 태그를 반환 - 일정 추가에서 자동으로 추천",
        description = "유저가 선택한 태그를 기반으로 추천 태그 3개를 반환합니다. (추천 서비스 장애시 랜덤 3개)",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "추천 태그 3개 반환 성공",
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
    "firstRecommendTag": { "content": "회의 참석" },
    "secondRecommendTag": { "content": "점심 식사 약속" },
    "thirdRecommendTag": { "content": "헬스장 운동" }
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
                description = "잘못된 요청 (ex. 로그인 정보 누락 등)",
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
    "message": "사용자가 태그를 3개이상 갖고있지 않습니다. 태그를 먼저 선택해주세요.",
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
    fun getTagRecommendMixed(
        @Parameter(hidden = true) @CurrentUserId userId: String,
    ): ApiResponse<TagApiResponses>

    @Operation(
        summary = "유저 태그 기반 완전히 새로운 추천 태그 3개 반환 - 마이페이지에서 할일 수정시 사용",
        description = """
        유저가 선택한 태그(기존 태그)와 중복되지 않는,
        완전히 새로운 태그 3개를 추천합니다.
        (추천 서비스 장애 또는 태그 미입력 시 에러 반환)
    """,
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "완전히 새로운 추천 태그 3개 반환 성공",
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
    "firstRecommendTag": { "content": "캠핑" },
    "secondRecommendTag": { "content": "플리마켓 구경" },
    "thirdRecommendTag": { "content": "카페 투어" }
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
                description = "잘못된 요청 (ex. 태그 3개 미만 등)",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "태그 미입력 에러 예시",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "사용자가 태그를 3개이상 갖고있지 않습니다. 태그를 먼저 선택해주세요.",
    "data": {}
  }
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "404",
                description = "추천 서버 장애로 인한 에러 (MCP 장애)",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "서버 장애 에러 예시",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "MCP_SERVER_TAGS_ERROR",
    "message": "MCP 추천 서버에서 장애가 발생했습니다. 새로운 태그를 추천할 수 없습니다.",
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
    fun getTagRecommendOnlyNew(
        @Parameter(hidden = true) @CurrentUserId userId: String,
    ): ApiResponse<TagApiResponses>

    @Operation(
        summary = "샌드위치 연휴 추천",
        description = """
        사용자 생일 및 공휴일 정보를 기반으로  
        샌드위치 연차(연휴 시작일·종료일) 기간을 추천합니다.
    """,
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "샌드위치 연차 기간 반환 성공",
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
    "startDate": "2025-07-14",
    "endDate": "2025-07-16"
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
                description = "잘못된 요청 (ex. 로그인 정보 누락 등)",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "잘못된 요청 예시",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "잘못된 요청입니다.",
    "data": {}
  }
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "생일 정보 없음",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "생일 정보 없음 에러 예시",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "USER_BIRTH_DAY_NOT_FOUND",
    "message": "사용자가 생일을 갖고있지 않습니다. 생일 먼저 추가해주세요.",
    "data": {}
  }
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "504",
                description = "MCP 추천 서버 무응답",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "MCP 서버 타임아웃 에러 예시",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "MCP_SERVER_SANDWICH_ERROR",
    "message": "MCP 추천 서버의 응답이 없습니다. 잠시 후 다시 시도해주세요.",
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
    fun getSandwich(
        @Parameter(hidden = true) @CurrentUserId userId: String,
    ): ApiResponse<SandwichResponse>
}

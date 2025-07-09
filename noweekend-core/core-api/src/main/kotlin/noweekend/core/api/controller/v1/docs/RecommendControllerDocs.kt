package noweekend.core.api.controller.v1.docs

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.client.mcp.recommend.model.TagApiResponses
import noweekend.core.api.controller.v1.response.WeatherApiResponse
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.support.response.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@Tag(
    name = "추천",
    description = "반차/연차 추천 API (향후 3일간 7~20시 눈·비 예보 기반)",
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
        "localDate": "2025-07-04",
        "recommendContent": "오후에 비 와요, 연차 어때요?"
      },
      {
        "localDate": "2025-07-06",
        "recommendContent": "오전 눈 예보, 반차 추천!"
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
    ): ApiResponse<WeatherApiResponse>

    @Operation(
        summary = "유저 태그 기반 추천 태그 3개 반환",
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
    fun getTagRecommend(
        @Parameter(hidden = true) @CurrentUserId userId: String,
    ): ApiResponse<TagApiResponses>
}

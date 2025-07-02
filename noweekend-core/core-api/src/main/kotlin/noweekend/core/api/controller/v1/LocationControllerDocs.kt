package noweekend.core.api.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.core.api.controller.v1.request.LocationRequest
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.support.response.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@Tag(name = "유저 위치", description = "유저 위치 정보 등록/수정 API")
interface LocationControllerDocs {

    @Operation(
        summary = "유저 위치 등록/수정",
        description = "현재 로그인한 유저의 위치 정보를 저장하거나 수정합니다.",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = LocationRequest::class),
                    examples = [
                        ExampleObject(
                            name = "예시 요청",
                            value = """
                                {
                                  "latitude": 37.5665,
                                  "longitude": 126.9780
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
                description = "위치 저장 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "SUCCESS",
  "data": "위치 저장됨: 37.5665, 126.9780",
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
    fun saveLocation(
        @Parameter(hidden = true) @CurrentUserId userId: String,
        @RequestBody request: LocationRequest,
    ): ApiResponse<String>
}

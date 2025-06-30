package noweekend.core.api.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.core.api.controller.v1.request.LoginRequest
import noweekend.core.api.controller.v1.response.GoogleLoginResponse
import noweekend.core.api.controller.v1.response.OAuthLoginResponse
import noweekend.core.support.response.ApiResponse

@Tag(name = "인증", description = "소셜 로그인 및 인증 API")
interface AuthControllerDocs {
    @Operation(
        summary = "소셜 로그인",
        description = "소셜 AccessToken을 이용한 회원 인증/가입 처리. 회원가입시 authorizationCode와 name을 함께 보내주셔야하고, 로그인시에는 authorizationCode 보내주시면됩니다.",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = LoginRequest::class),
                ),
            ],
        ),
        responses = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GoogleLoginResponse::class),
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
    fun loginWithGoogle(providerType: String, request: LoginRequest): ApiResponse<OAuthLoginResponse>
}

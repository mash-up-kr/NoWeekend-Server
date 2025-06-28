package noweekend.core.api.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.core.api.controller.v1.request.LoginRequest
import noweekend.core.api.controller.v1.request.toOAuthLoginParams
import noweekend.core.api.controller.v1.response.GoogleLoginResponse
import noweekend.core.api.controller.v1.response.OAuthLoginResponse
import noweekend.core.domain.auth.AuthApplicationService
import noweekend.core.domain.enumerate.ProviderType
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerRequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@Tag(name = "인증", description = "소셜 로그인 및 인증 API")
@RestController
@RequestMapping("/api/v1/login")
class AuthController(
    private val authApplicationService: AuthApplicationService,
) {

    @Operation(
        summary = "소셜 로그인",
        description = "소셜 AccessToken을 이용한 회원 인증/가입 처리. 회원가입시 authorizationCode와 name을 함께 보내주셔야하고, 로그인시에는 authorizationCode 보내주시면됩니다.",
        requestBody = SwaggerRequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = LoginRequest::class),
                ),
            ],
        ),
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GoogleLoginResponse::class),
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
            ),
        ],
    )
    @PostMapping("/{providerType}")
    fun loginWithGoogle(
        @PathVariable("providerType") providerType: ProviderType,
        @Validated @RequestBody request: LoginRequest,
    ): ApiResponse<OAuthLoginResponse> {
        return ApiResponse.success(
            authApplicationService.signInForSocial(request.toOAuthLoginParams(providerType), request.name),
        )
    }
}

package noweekend.core.api.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.client.google.GoogleClient
import noweekend.client.google.GoogleTokensRequest
import noweekend.core.api.controller.v1.request.LoginRequest
import noweekend.core.api.controller.v1.response.GoogleLoginResponse
import noweekend.core.domain.auth.AuthService
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
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
    private val authService: AuthService,
    private val googleClient: GoogleClient,
) {

    @Operation(
        summary = "구글 로그인",
        description = "구글 AccessToken을 이용한 회원 인증/가입 처리. 회원가입시 google accessToken과 name을 함께 보내주셔야하고, 로그인시에는 google accessToken만 보내주시면됩니다.",
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
    @PostMapping("/google")
    fun loginWithGoogle(
        @Validated @RequestBody req: LoginRequest,
    ): ApiResponse<GoogleLoginResponse> {
        val apiResult = googleClient.getEmail(
            GoogleTokensRequest(req.accessToken),
        )
        val result = authService.googleLogin(
            email = apiResult.email,
            name = req.name,
        )
        return ApiResponse.success(
            GoogleLoginResponse(
                email = apiResult.email,
                exists = result.exists,
                accessToken = result.accessToken,
            ),
        )
    }
}

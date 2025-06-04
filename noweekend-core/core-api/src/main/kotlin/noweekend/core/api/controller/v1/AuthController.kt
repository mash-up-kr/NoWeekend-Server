package noweekend.core.api.controller.v1

import noweekend.client.google.GoogleUserInfoClient
import noweekend.client.google.GoogleUserInfoRequest
import noweekend.core.api.controller.v1.request.LoginRequest
import noweekend.core.api.controller.v1.response.GoogleLoginResponse
import noweekend.core.domain.auth.AuthService
import noweekend.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/login")
class AuthController(
    private val authService: AuthService,
    private val googleUserInfoClient: GoogleUserInfoClient,
    // ToDo InfoClient 명시
) {
    @PostMapping("/google")
    fun loginWithGoogle(
        @RequestBody req: LoginRequest,
    ): ApiResponse<GoogleLoginResponse> {
        val apiResult = googleUserInfoClient.getEmail(
            GoogleUserInfoRequest(req.accessToken),
        )
        val result = authService.googleLogin(apiResult.email)
        return ApiResponse.success(
            GoogleLoginResponse(result.email, result.exists),
        )
    }
}

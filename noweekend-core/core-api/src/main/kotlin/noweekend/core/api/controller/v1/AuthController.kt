package noweekend.core.api.controller.v1

import noweekend.core.api.controller.v1.request.LoginRequest
import noweekend.core.api.controller.v1.request.toOAuthLoginParams
import noweekend.core.api.controller.v1.response.OAuthLoginResponse
import noweekend.core.domain.auth.AuthApplicationService
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/login")
class AuthController(
    private val authApplicationService: AuthApplicationService,
) : AuthControllerDocs {

    @PostMapping("/{providerType}")
    override fun loginWithGoogle(
        @PathVariable("providerType") providerType: String,
        @Validated @RequestBody request: LoginRequest,
    ): ApiResponse<OAuthLoginResponse> {
        return ApiResponse.success(
            authApplicationService.signInForSocial(request.toOAuthLoginParams(providerType), request.name),
        )
    }
}

package noweekend.core.api.controller.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import noweekend.client.apple.AppleLoginParams
import noweekend.client.common.OAuthLoginParams
import noweekend.client.google.GoogleLoginParams
import noweekend.core.domain.enumerate.ProviderType
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType

@Schema(description = "소셜 로그인 요청 DTO")
data class LoginRequest(
    @field:NotBlank(message = "authorizationCode는 필수입니다.")
    @field:Schema(description = "OAuth Authorization Code")
    val authorizationCode: String,

    @field:Schema(description = "사용자 이름. 회원가입시 필수, 로그인시 null로 보내시면됩니다.", example = "홍길동")
    val name: String?,
)

fun LoginRequest.toOAuthLoginParams(providerTypeStr: String): OAuthLoginParams {
    val providerType = try {
        ProviderType.valueOf(providerTypeStr.uppercase())
    } catch (e: Exception) {
        throw CoreException(
            ErrorType.INVALID_PROVIDER_TYPE,
        )
    }

    return when (providerType) {
        ProviderType.GOOGLE -> GoogleLoginParams(authorizationCode)
        ProviderType.APPLE -> AppleLoginParams(authorizationCode)
    }
}

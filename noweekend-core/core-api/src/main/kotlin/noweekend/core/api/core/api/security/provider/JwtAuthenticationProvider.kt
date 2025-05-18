package noweekend.core.api.core.api.security.provider

import noweekend.core.api.core.api.security.authentication.AuthenticatedUserToken
import noweekend.core.api.core.api.security.authentication.UserAuthenticationToken
import noweekend.core.api.core.api.security.jwt.JwtProvider
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(private val jwtProvider: JwtProvider) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication.credentials as String

        // TODO: Replace illegal argument exception with custom exception
        if (!jwtProvider.validate(token)) {
            throw IllegalArgumentException("Invalid JWT")
        }

        val userId = jwtProvider.getUserIdFromToken(token)
        val authenticated = AuthenticatedUserToken(token, userId.toString())

        return authenticated
    }

    override fun supports(authentication: Class<*>): Boolean =
        UserAuthenticationToken::class.java.isAssignableFrom(authentication)
}

package noweekend.core.api.core.api.security.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken

class AuthenticatedUserToken(private val token: String, val principal: String): AbstractAuthenticationToken(null) {
    override fun getCredentials(): Any = token
    override fun getPrincipal(): Any = principal
    override fun isAuthenticated(): Boolean = true
}
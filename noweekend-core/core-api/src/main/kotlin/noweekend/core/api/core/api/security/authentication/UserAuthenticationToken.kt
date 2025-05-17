package noweekend.core.api.core.api.security.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken

class UserAuthenticationToken(private val token: String) : AbstractAuthenticationToken(null) {
    override fun getCredentials(): Any = token
    override fun getPrincipal(): Any = token
    override fun isAuthenticated(): Boolean = false
}
package noweekend.core.api.core.api.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import noweekend.core.api.core.api.security.authentication.UserAuthenticationToken
import noweekend.core.api.core.support.error.ErrorMessage
import noweekend.core.api.core.support.error.ErrorType
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val objectMapper: ObjectMapper,
    private val authenticationManager: AuthenticationManager,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        runCatching {
            setAuthenticationFromToken(request)
        }.onFailure { exception ->
            SecurityContextHolder.clearContext()
            response.apply {
                status = HttpStatus.UNAUTHORIZED.value()
                addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                val failResponse = ErrorMessage(ErrorType.UNAUTHORIZED)
                writer.write(objectMapper.writeValueAsString(failResponse))
            }
        }.onSuccess {
            filterChain.doFilter(request, response)
        }
    }

    private fun setAuthenticationFromToken(request: HttpServletRequest) {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)?.substringAfter(PREFIX_BEARER)
        if (token.isNullOrBlank()) {
            throw BadCredentialsException("invalid access token")
        }
        val authRequest = UserAuthenticationToken(token)
        val authenticated = authenticationManager.authenticate(authRequest)
        if (!authenticated.isAuthenticated) {
            throw AuthorizationDeniedException("Invalid ${authRequest::class.simpleName}")
        }
        val context = SecurityContextHolder.createEmptyContext().apply {
            authentication = authenticated
        }
        SecurityContextHolder.setContext(context)
    }

    companion object {
        private const val PREFIX_BEARER = "Bearer "
    }
}
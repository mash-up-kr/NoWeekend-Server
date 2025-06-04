package noweekend.core.domain.auth

import noweekend.core.domain.user.UserReader
import noweekend.core.domain.user.UserWriter
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userWriter: UserWriter,
    private val userReader: UserReader,
) {
    fun googleLogin(email: String): AuthResult {
        TODO("Not yet implemented")
    }
}

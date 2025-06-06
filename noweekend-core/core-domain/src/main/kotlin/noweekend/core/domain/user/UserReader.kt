package noweekend.core.domain.user

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class UserReader(
    private val userRepository: UserRepository,
) {
    fun findUserById(id: String): User {
        return userRepository.findUserById(id)
    }

    fun findUserProviderAndEmail(provider: ProviderType, email: String): User? {
        return userRepository.findUserByProviderAndEmail(provider, email)
    }
}

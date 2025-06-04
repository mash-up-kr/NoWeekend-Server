package noweekend.core.domain.user

import org.springframework.stereotype.Component

@Component
class UserReader(
    private val userRepository: UserRepository,
) {
    fun findById(id: String): User {
        return userRepository.findUserById(id)
    }
}

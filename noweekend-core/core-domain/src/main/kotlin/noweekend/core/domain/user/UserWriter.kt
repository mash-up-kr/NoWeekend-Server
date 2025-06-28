package noweekend.core.domain.user

import noweekend.core.domain.enumerate.ProviderType
import noweekend.core.domain.enumerate.Role
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserWriter(
    private val userRepository: UserRepository,
) {
    fun append(
        id: String,
        name: String,
        providerType: ProviderType,
        role: Role,
    ): String = userRepository.append(id, name, providerType, role)

    fun upsert(
        user: User,
    ): User = userRepository.upsert(user)

    fun modify(
        id: String,
        name: String,
        role: Role,
    ): String = userRepository.modify(id, name, role)

    fun register(user: User): User = userRepository.register(user)
}

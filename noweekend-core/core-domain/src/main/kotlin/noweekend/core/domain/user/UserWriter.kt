package noweekend.core.domain.user

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
        id: String,
        name: String,
        providerType: ProviderType,
        role: Role,
    ): String = userRepository.upsert(id, name, providerType, role)

    fun modify(
        id: String,
        name: String,
        role: Role,
    ): String = userRepository.modify(id, name, role)

    fun register(user: User): User = userRepository.register(user)
}

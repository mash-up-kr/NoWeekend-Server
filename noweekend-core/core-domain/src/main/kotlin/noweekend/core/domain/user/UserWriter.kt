package noweekend.core.domain.user

import org.springframework.stereotype.Component

@Component
class UserWriter(
    private val userRepository: UserRepository,
) {
    fun append(
        id: String,
        profileImg: String,
        name: String,
        providerType: ProviderType,
        role: Role,
    ): String = userRepository.append(id, profileImg, name, providerType, role)

    fun upsert(
        id: String,
        profileImg: String,
        name: String,
        providerType: ProviderType,
        role: Role,
    ): String = userRepository.upsert(id, profileImg, name, providerType, role)

    fun modify(
        id: String,
        profileImg: String,
        name: String,
        role: Role,
    ): String = userRepository.modify(id, profileImg, name, role)
}

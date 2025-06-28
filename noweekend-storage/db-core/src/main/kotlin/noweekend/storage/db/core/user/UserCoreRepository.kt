package noweekend.storage.db.core.user

import noweekend.core.domain.enumerate.ProviderType
import noweekend.core.domain.enumerate.Role
import noweekend.core.domain.user.User
import noweekend.core.domain.user.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepository(
    private val jpaRepository: UserJpaRepository,
    private val queryDslRepository: UserQueryDslRepository,
) : UserRepository {
    override fun findUserById(id: String): User {
        return jpaRepository.findById(id)
            .orElseThrow { NoSuchElementException("User not found: $id") }
            .toUser()
    }

    override fun findUserByProviderAndProviderId(
        providerType: ProviderType,
        providerId: String,
    ): User {
        val userEntity = queryDslRepository.findUserByProviderAndProviderId(providerType, providerId)
            ?: throw NoSuchElementException("User not found: $providerType, $providerId")
        return userEntity.toUser()
    }

    override fun append(id: String, name: String, providerType: ProviderType, role: Role): String {
        TODO("Not yet implemented")
    }

    override fun modify(id: String, name: String, role: Role): String {
        TODO("Not yet implemented")
    }

    override fun upsert(id: String, name: String, providerType: ProviderType, role: Role): String {
        TODO("Not yet implemented")
    }

    override fun register(user: User): User {
        val userEntity = user.toEntity()
        val saveUserEntity = jpaRepository.save(userEntity)
        return saveUserEntity.toUser()
    }
}

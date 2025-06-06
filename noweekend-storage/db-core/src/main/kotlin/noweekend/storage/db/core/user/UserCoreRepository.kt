package noweekend.storage.db.core.user

import noweekend.core.domain.user.ProviderType
import noweekend.core.domain.user.Role
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

    override fun findUserByProviderAndEmail(providerType: ProviderType, email: String): User {
        val userEntity = queryDslRepository.findUserByProviderAndEmail(providerType, email)
            ?: throw NoSuchElementException("User not found: $providerType, $email")
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

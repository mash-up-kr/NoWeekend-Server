package noweekend.storage.db.core.user

import noweekend.core.domain.enumerate.ProviderType
import noweekend.core.domain.user.Location
import noweekend.core.domain.user.User
import noweekend.core.domain.user.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepository(
    private val jpaRepository: UserJpaRepository,
    private val queryDslRepository: UserQueryDslRepository,
) : UserRepository {
    override fun findUserById(id: String): User? {
        return jpaRepository.findByIdAndDeletedFalse(id)?.toUser()
    }

    override fun findUserByProviderAndProviderId(
        providerType: ProviderType,
        providerId: String,
    ): User? {
        return queryDslRepository.findUserByProviderAndProviderId(providerType, providerId)?.toUser()
    }

    override fun upsert(user: User): User {
        return jpaRepository.save(user.toEntity()).toUser()
    }

    override fun register(user: User): User {
        val userEntity = user.toEntity()
        val saveUserEntity = jpaRepository.save(userEntity)
        return saveUserEntity.toUser()
    }

    override fun findLocationByUserId(userId: String): Location? {
        return jpaRepository.findLocationByUserId(userId)?.toDomain()
    }

    override fun delete(user: User) {
        jpaRepository.save(user.toEntity())
    }

    override fun findUserByEmail(email: String): User? {
        return queryDslRepository.findUserByEmail(email)?.toUser()
    }
}

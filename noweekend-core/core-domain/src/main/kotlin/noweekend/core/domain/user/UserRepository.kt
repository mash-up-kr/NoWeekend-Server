package noweekend.core.domain.user

import noweekend.core.domain.enumerate.ProviderType

interface UserRepository {
    fun findUserById(id: String): User?
    fun findUserByProviderAndProviderId(providerType: ProviderType, providerId: String): User?
    fun register(user: User): User
    fun upsert(user: User): User
    fun findLocationByUserId(userId: String): Location?
    fun delete(user: User)
    fun findUserByEmail(email: String): User?
}

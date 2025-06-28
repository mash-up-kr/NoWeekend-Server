package noweekend.core.domain.user

import noweekend.core.domain.enumerate.ProviderType
import noweekend.core.domain.enumerate.Role

interface UserRepository {
    fun findUserById(id: String): User
    fun findUserByProviderAndProviderId(providerType: ProviderType, providerId: String): User
    fun append(id: String, name: String, providerType: ProviderType, role: Role): String
    fun modify(id: String, name: String, role: Role): String
    fun upsert(id: String, name: String, providerType: ProviderType, role: Role): String
    fun register(user: User): User
}

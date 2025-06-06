package noweekend.core.domain.user

interface UserRepository {
    fun findUserById(id: String): User
    fun findUserByProviderAndEmail(providerType: ProviderType, email: String): User
    fun append(id: String, name: String, providerType: ProviderType, role: Role): String
    fun modify(id: String, name: String, role: Role): String
    fun upsert(id: String, name: String, providerType: ProviderType, role: Role): String
    fun register(user: User): User
}

package noweekend.core.domain.user

interface UserRepository {
    fun findUserById(id: String): User
    fun append(id: String, profileImg: String, name: String, providerType: ProviderType, role: Role): String
    fun modify(id: String, profileImg: String, name: String, role: Role): String
    fun upsert(id: String, profileImg: String, name: String, providerType: ProviderType, role: Role): String
}

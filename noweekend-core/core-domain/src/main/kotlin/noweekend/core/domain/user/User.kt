package noweekend.core.domain.user

import noweekend.core.domain.util.IdGenerator
import java.time.LocalDateTime

data class User(
    val id: String,
    val email: String,
    var name: String?,
    var gender: Gender = Gender.NONE,
    val providerType: ProviderType,
    val role: Role,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun newUser(
            email: String,
            name: String,
            providerType: ProviderType,
            role: Role,
        ): User {
            return User(
                id = IdGenerator.generate(),
                email = email,
                name = name,
                providerType = providerType,
                role = role,
                createdAt = null,
                updatedAt = null,
            )
        }
    }
}

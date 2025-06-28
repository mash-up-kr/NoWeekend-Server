package noweekend.core.domain.user

import noweekend.core.domain.enumerate.Gender
import noweekend.core.domain.enumerate.ProviderType
import noweekend.core.domain.enumerate.Role
import noweekend.core.domain.util.IdGenerator
import java.time.LocalDate
import java.time.LocalDateTime

data class User(
    val id: String,
    val email: String?,
    var name: String?,
    var gender: Gender = Gender.NONE,
    val providerId: String,
    val providerType: ProviderType,
    val revocableToken: String?,
    val role: Role,
    val birthDate: LocalDate?,
    val remainingAnnualLeave: Double = 0.0,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun newUser(
            providerId: String,
            email: String?,
            name: String,
            providerType: ProviderType,
            revocableToken: String?,
            role: Role,
        ): User {
            return User(
                id = IdGenerator.generate(),
                providerId = providerId,
                email = email,
                name = name,
                providerType = providerType,
                revocableToken = revocableToken,
                role = role,
                birthDate = null,
                createdAt = null,
                updatedAt = null,
            )
        }
    }
}

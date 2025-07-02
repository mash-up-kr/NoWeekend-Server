package noweekend.core.domain.auth

import noweekend.core.domain.enumerate.Gender
import noweekend.core.domain.enumerate.ProviderType
import noweekend.core.domain.enumerate.Role
import noweekend.core.domain.jwt.JwtProvider
import noweekend.core.domain.user.User
import noweekend.core.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Service
class TestUserService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
) {

    @Transactional
    fun testUserGen(): UserWithToken {
        val uuid = getUuid()

        val testUser = User.newUser(
            providerId = generateTestProviderId(uuid),
            email = generateTestEmail(uuid),
            name = generateTestName(uuid),
            providerType = ProviderType.GOOGLE,
            revocableToken = null,
            role = Role.USER,
        )

        userRepository.register(testUser)
        val ourAccessToken = jwtProvider.generate(testUser.id)
        return UserWithToken.from(
            user = testUser,
            jwtToken = ourAccessToken,
        )
    }

    private fun generateTestEmail(uuid: String): String {
        return "test-user-$uuid@gmail.com"
    }

    private fun generateTestProviderId(uuid: String): String {
        return "provider-id-test-$uuid"
    }

    private fun generateTestName(uuid: String): String {
        return "test-user-$uuid"
    }

    private fun getUuid(): String {
        return UUID.randomUUID().toString().substring(0, 8)
    }
}

data class UserWithToken(
    val id: String,
    val jwtToken: String,
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
        fun from(user: User, jwtToken: String): UserWithToken {
            return UserWithToken(
                id = user.id,
                jwtToken = jwtToken,
                email = user.email,
                name = user.name,
                gender = user.gender,
                providerId = user.providerId,
                providerType = user.providerType,
                revocableToken = user.revocableToken,
                role = user.role,
                birthDate = user.birthDate,
                remainingAnnualLeave = user.remainingAnnualLeave,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
            )
        }
    }
}

package noweekend.core.api.controller.v1.response

import noweekend.core.domain.enumerate.Gender
import noweekend.core.domain.enumerate.ProviderType
import noweekend.core.domain.enumerate.Role
import noweekend.core.domain.user.Location
import noweekend.core.domain.user.User
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType
import java.time.LocalDate
import java.time.LocalDateTime

data class UserInformationResponse(
    val id: String,
    val email: String?,
    var name: String?,
    var gender: Gender,
    val providerId: String,
    val providerType: ProviderType,
    val revocableToken: String?,
    val role: Role,
    val birthDate: LocalDate,
    val remainingAnnualLeave: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    var location: Location,
) {
    companion object {
        fun from(user: User): UserInformationResponse {
            return UserInformationResponse(
                id = user.id,
                email = user.email,
                name = user.name,
                gender = user.gender,
                providerId = user.providerId,
                providerType = user.providerType,
                revocableToken = user.revocableToken,
                role = user.role,
                birthDate = user.birthDate ?: throw CoreException(ErrorType.USER_BIRTH_DAY_NOT_FOUND),
                remainingAnnualLeave = user.remainingAnnualLeave,
                createdAt = user.createdAt ?: throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL),
                updatedAt = user.updatedAt,
                location = user.location ?: throw CoreException(ErrorType.USER_LOCATION_NOT_FOUND),
            )
        }
    }
}

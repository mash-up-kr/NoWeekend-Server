package noweekend.storage.db.core.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import noweekend.core.domain.enumerate.Gender
import noweekend.core.domain.enumerate.ProviderType
import noweekend.core.domain.enumerate.Role
import noweekend.core.domain.user.User
import noweekend.storage.db.core.BaseEntity
import java.time.LocalDate

@Entity
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_users_email", columnNames = ["email"]),
        UniqueConstraint(name = "uk_users_provider_type_provider_id", columnNames = ["provider_type", "provider_id"]),
    ],
)
class UserEntity(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "email", length = 100, nullable = true, unique = true)
    val email: String?,

    @Column(name = "name", length = 20)
    var name: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10, nullable = false)
    var gender: Gender = Gender.NONE,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", length = 10, nullable = false)
    val providerType: ProviderType,

    @Column(name = "provider_id", length = 100, nullable = false)
    val providerId: String,

    @Column(name = "revocable_token", length = 100, nullable = true)
    val revocableToken: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 10, nullable = false)
    val role: Role,

    @Column(name = "birth_date", nullable = true)
    val birthDate: LocalDate? = null,
) : BaseEntity() {
    fun toUser(): User = User(
        id = this.id,
        email = this.email,
        name = this.name,
        gender = this.gender,
        providerType = this.providerType,
        providerId = this.providerId,
        revocableToken = this.revocableToken,
        role = this.role,
        birthDate = this.birthDate,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}

fun User.toEntity(): UserEntity = UserEntity(
    id = this.id,
    email = this.email,
    name = this.name,
    gender = this.gender,
    providerType = this.providerType,
    providerId = this.providerId,
    revocableToken = this.revocableToken,
    role = this.role,
    birthDate = this.birthDate,
)

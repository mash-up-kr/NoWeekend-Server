package noweekend.storage.db.core.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import noweekend.core.domain.user.Gender
import noweekend.core.domain.user.ProviderType
import noweekend.core.domain.user.Role
import noweekend.core.domain.user.User
import noweekend.storage.db.core.BaseEntity

@Entity
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_users_email", columnNames = ["email"]),
    ],
)
class UserEntity(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "email", length = 100, nullable = false, unique = true)
    val email: String,

    @Column(name = "name", length = 20)
    var name: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10, nullable = false)
    var gender: Gender = Gender.NONE,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", length = 10, nullable = false)
    val providerType: ProviderType,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 10, nullable = false)
    val role: Role,
) : BaseEntity() {
    fun toUser(): User = User(
        id = this.id,
        email = this.email,
        name = this.name,
        gender = this.gender,
        providerType = this.providerType,
        role = this.role,
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
    role = this.role,
)

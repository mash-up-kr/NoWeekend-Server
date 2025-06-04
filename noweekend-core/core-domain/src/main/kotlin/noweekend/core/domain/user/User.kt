package noweekend.core.domain.user

import java.time.LocalDateTime

data class User(
    val id: String,
    val profileImg: String,
    val name: String,
    val providerType: ProviderType,
    val role: Role,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)

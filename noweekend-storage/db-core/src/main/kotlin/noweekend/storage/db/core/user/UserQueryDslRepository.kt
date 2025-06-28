package noweekend.storage.db.core.user

import com.querydsl.jpa.impl.JPAQueryFactory
import noweekend.core.domain.enumerate.ProviderType
import org.springframework.stereotype.Repository

@Repository
class UserQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory,
) {

    fun findUserByProviderAndProviderId(providerType: ProviderType, providerId: String): UserEntity? {
        val userEntity = QUserEntity.userEntity
        return jpaQueryFactory
            .selectFrom(userEntity)
            .where(userEntity.providerId.eq(providerId), userEntity.providerType.eq(providerType))
            .fetchOne()
    }
}

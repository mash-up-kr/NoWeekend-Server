package noweekend.storage.db.core.user

import com.querydsl.jpa.impl.JPAQueryFactory
import noweekend.core.domain.user.ProviderType
import org.springframework.stereotype.Repository

@Repository
class UserQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory,
) {

    fun findUserByProviderAndEmail(providerType: ProviderType, email: String): UserEntity? {
        val userEntity = QUserEntity.userEntity
        return jpaQueryFactory
            .selectFrom(userEntity)
            .where(userEntity.email.eq(email), userEntity.providerType.eq(providerType))
            .fetchOne()
    }
}

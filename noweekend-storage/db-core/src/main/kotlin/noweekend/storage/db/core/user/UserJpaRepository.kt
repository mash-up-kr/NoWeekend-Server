package noweekend.storage.db.core.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserJpaRepository : JpaRepository<UserEntity, String> {
    @Query("SELECT u.location FROM UserEntity u WHERE u.id = :userId AND u.deleted = false")
    fun findLocationByUserId(@Param("userId") userId: String): LocationEmbeddable?

    fun findByIdAndDeletedFalse(id: String): UserEntity?
}

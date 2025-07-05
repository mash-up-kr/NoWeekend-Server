package noweekend.storage.db.core.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserJpaRepository : JpaRepository<UserEntity, String> {
    @Query("SELECT u.location FROM UserEntity u WHERE u.id = :userId")
    fun findLocationByUserId(@Param("userId") userId: String): LocationEmbeddable?
}

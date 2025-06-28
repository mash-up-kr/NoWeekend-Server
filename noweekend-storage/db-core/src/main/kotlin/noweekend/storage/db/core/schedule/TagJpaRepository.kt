package noweekend.storage.db.core.schedule

import org.springframework.data.jpa.repository.JpaRepository

interface TagJpaRepository : JpaRepository<TagHistoryEntity, String>

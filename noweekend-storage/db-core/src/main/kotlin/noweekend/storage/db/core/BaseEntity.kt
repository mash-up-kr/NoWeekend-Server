package noweekend.storage.db.core

import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.MIN

    @UpdateTimestamp
    val updatedAt: LocalDateTime = LocalDateTime.MIN

    val isDeleted: Boolean = false
}

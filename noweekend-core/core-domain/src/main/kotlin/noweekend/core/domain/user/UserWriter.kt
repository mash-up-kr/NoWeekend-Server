package noweekend.core.domain.user

import noweekend.core.domain.tag.ScheduleRepository
import noweekend.core.domain.tag.TagRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class UserWriter(
    private val userRepository: UserRepository,
    private val scheduleRepository: ScheduleRepository,
    private val tagHistoryRepository: TagRepository,
) {

    fun upsert(
        user: User,
    ): User = userRepository.upsert(user)

    fun register(user: User): User = userRepository.register(user)

    fun delete(userId: String) {
        val findUser = userRepository.findUserById(userId) ?: throw NoSuchElementException("사용자가 존재하지 않음")
        val deleteUser = findUser.copy(
            deleted = true,
        )
        userRepository.delete(deleteUser)

        scheduleRepository.markDeletedByUserId(userId)
        tagHistoryRepository.markDeletedByUserId(userId)
    }
}

package noweekend.core.domain.weekend

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class WeekendWriter(
    private val weekendRepository: WeekendRepository,
) {

    fun register(weekend: Weekend) {
        weekendRepository.register(weekend)
    }
}

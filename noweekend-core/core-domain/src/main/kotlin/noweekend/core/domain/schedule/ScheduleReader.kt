package noweekend.core.domain.schedule

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class ScheduleReader(
    private val scheduleRepository: ScheduleRepository,
)

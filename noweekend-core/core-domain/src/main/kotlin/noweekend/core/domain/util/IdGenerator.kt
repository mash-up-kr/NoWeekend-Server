package noweekend.core.domain.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

object IdGenerator {
    private const val SEOUL = "Asia/Seoul"
    private const val TIME_FORMAT = "yyyyMMddHHmmss"

    private val seoul: ZoneId = ZoneId.of(SEOUL)
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT)

    fun generate(): String {
        val now = LocalDateTime.now(seoul).format(formatter)
        val uuid = UUID.randomUUID().toString().replace("-", "")
        return "v1-$now-$uuid"
    }
}

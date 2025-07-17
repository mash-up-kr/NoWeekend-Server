package noweekend.core.domain.holiday

import noweekend.client.mcp.holiday.HolidayClient
import noweekend.client.mcp.holiday.model.HolidayRequest
import noweekend.core.api.controller.v1.response.HolidayResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class HolidayServiceImpl(
    private val holidayClient: HolidayClient,
    private val holidayWriter: HolidayWriter,
    private val holidayReader: HolidayReader,
) : HolidayService {

    override fun getMonthHolidays(
        year: Int,
        month: Int,
    ): List<HolidayResponse> {
        return holidayReader.findMonthHolidays(year, month)
            .map { HolidayResponse.from(it) }
    }

    override fun getRemainingHolidays(): List<HolidayResponse> {
        val today = LocalDate.now()
        return holidayReader.findRemainingHolidays(today)
            .map { HolidayResponse.from(it) }
    }

    override fun updateHolidays(year: Int) {
        syncHolidays(year)
        log.info("[HolidayService] ${year}년도 공휴일을 오늘자로 동기화 완료")
    }

    private fun syncHolidays(year: Int): List<Holiday> {
        val saved = holidayReader.findAllByYear(year)

        val existing = saved
            .map { Triple(it.month, it.day, it.content) to it.dayOfWeekKor }
            .toSet()

        val fetched = (1..12).flatMap { month ->
            holidayClient
                .getHolidays(HolidayRequest(year, month))
                .map { it.toDomain(year) }
        }

        // 4) DB에 없는 것만 필터링해서 저장
        val toSave = fetched.filter { h ->
            Pair(Triple(h.month, h.day, h.content), h.dayOfWeekKor) !in existing
        }
        toSave.forEach {
            holidayWriter.register(it)
        }

        return (saved + toSave)
            .distinctBy { Triple(it.month, it.day, it.content) }
            .sortedWith(compareBy({ it.month }, { it.day }))
    }

    companion object {
        private val log = LoggerFactory.getLogger(HolidayServiceImpl::class.java)
    }
}

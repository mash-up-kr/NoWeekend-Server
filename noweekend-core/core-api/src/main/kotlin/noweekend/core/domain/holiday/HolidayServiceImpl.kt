package noweekend.core.domain.holiday

import noweekend.client.mcp.holiday.HolidayClient
import noweekend.client.mcp.holiday.model.HolidayRequest
import noweekend.client.mcp.holiday.model.toDomain
import noweekend.core.api.controller.v1.response.HolidayResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class HolidayServiceImpl(
    private val holidayClient: HolidayClient,
    private val holidayRepository: HolidayRepository,
) : HolidayService {

    override fun getThisYearHolidays(): List<HolidayResponse> {
        val today = LocalDate.now()
        val year = today.year
        val dbHolidays = holidayRepository.findAllByYear(year)

        // 오늘 생성된(업데이트된) 월
        val monthsInDbToday = dbHolidays
            .groupBy { it.month }
            .filterValues { list -> list.any { it.updatedAt.toLocalDate() == today } }
            .keys

        // 1~12월 모두 오늘자면 DB 데이터 반환
        if ((1..12).all { it in monthsInDbToday }) {
            return dbHolidays
                .distinctBy { it.month to it.content }
                .sortedBy { it.month }
                .map { HolidayResponse.from(it) }
        }

        // 아니면 외부 API에서 받아와 저장 후 반환
        val holidays = (1..12).flatMap { month ->
            holidayClient.getHolidays(HolidayRequest(year, month))
                .map { it.toDomain() }
                .onEach { holidayRepository.save(it) }
        }

        return holidays
            .distinctBy { it.month to it.content }
            .sortedBy { it.month }
            .map { HolidayResponse.from(it) }
    }

    override fun updateHolidays(year: Int) {
        val today = LocalDate.now()
        val dbHolidays = holidayRepository.findAllByYear(year)
        val monthsInDbToday = dbHolidays
            .groupBy { it.month }
            .filterValues { list -> list.any { it.updatedAt.toLocalDate() == today } }
            .keys

        if ((1..12).all { it in monthsInDbToday }) {
            log.info("[HolidayService] ${year}년도 공휴일은 이미 오늘 동기화되어 있음. updateHolidays 중단")
            return
        }

        (1..12).forEach { month ->
            holidayClient.getHolidays(HolidayRequest(year, month))
                .map { it.toDomain() }
                .forEach { holidayRepository.save(it) }
        }
        log.info("[HolidayService] ${year}년도 공휴일을 오늘자로 동기화 완료")
    }

    companion object {
        private val log = LoggerFactory.getLogger(HolidayServiceImpl::class.java)
    }
}

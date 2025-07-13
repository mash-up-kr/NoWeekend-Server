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
    private val holidayRepository: HolidayRepository,
) : HolidayService {

    override fun getThisMonthHolidays(): List<HolidayResponse> {
        val now = LocalDate.now()
        val year = now.year
        val month = now.monthValue

        return holidayRepository.findAllByYear(year)
            .asSequence()
            .filter { it.month == month }
            .sortedBy { it.day }
            .map { HolidayResponse.from(it) }
            .toList()
    }

    override fun updateHolidays(year: Int) {
        syncHolidays(year)
        log.info("[HolidayService] ${year}년도 공휴일을 오늘자로 동기화 완료")
    }

    private fun syncHolidays(year: Int): List<Holiday> {
        // 1) DB에 이미 저장된 공휴일 불러오기
        val saved = holidayRepository.findAllByYear(year)

        // 2) 기존에 저장된 키 집합 생성 ((month, day, content) + dayOfWeekKor)
        val existing = saved
            .map { Triple(it.month, it.day, it.content) to it.dayOfWeekKor }
            .toSet()

        // 3) 외부 API 호출 → Domain 객체 리스트
        val fetched = (1..12).flatMap { month ->
            holidayClient
                .getHolidays(HolidayRequest(year, month))
                .map { it.toDomain(year) }
        }

        // 4) DB에 없는 것만 필터링해서 저장
        val toSave = fetched.filter { h ->
            Pair(Triple(h.month, h.day, h.content), h.dayOfWeekKor) !in existing
        }
        toSave.forEach { holidayRepository.save(it) }

        return (saved + toSave)
            .distinctBy { Triple(it.month, it.day, it.content) }
            .sortedWith(compareBy({ it.month }, { it.day }))
    }

    companion object {
        private val log = LoggerFactory.getLogger(HolidayServiceImpl::class.java)
    }
}

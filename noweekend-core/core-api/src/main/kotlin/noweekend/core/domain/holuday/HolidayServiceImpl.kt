package noweekend.core.domain.holiday

import noweekend.client.holiday.HolidayClient
import noweekend.client.holiday.model.HolidayRequest
import noweekend.client.holiday.model.toDomain
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class HolidayServiceImpl(
    private val holidayClient: HolidayClient,
    private val holidayRepository: HolidayRepository
) {

    fun getTodayYearHolidays(year: Int): List<Holiday> {
        val today = LocalDate.now()

        val dbHolidays = holidayRepository.findAllByYear(year)

        val monthsInDbToday = dbHolidays
            .groupBy { it.month }
            .filter { (_, holidays) ->
                holidays.any { it.updatedAt.toLocalDate() == today }
            }
            .keys

        val missingMonths = (1..12).filter { it !in monthsInDbToday }

        val fetchedHolidays = mutableListOf<Holiday>()
        if (missingMonths.isNotEmpty()) {
            missingMonths.forEach { month ->
                val apiHolidays = holidayClient.getHolidays(
                    HolidayRequest(year, month)
                ).map { it.toDomain() }
                apiHolidays.forEach { holidayRepository.save(it) }
                fetchedHolidays.addAll(apiHolidays)
            }
        }

        return (dbHolidays + fetchedHolidays)
            .distinctBy { it.month to it.content }
            .sortedBy { it.month }
    }

    fun updateHolidays(year: Int) {
        for (month in 1..12) {
            val apiHolidays = holidayClient.getHolidays(
                HolidayRequest(year, month)
            ).map { it.toDomain() }
            apiHolidays.forEach { holiday ->
                val existing = holidayRepository.findByYearAndMonthAndContentAndLocalDate(
                    holiday.year, holiday.month, holiday.content, holiday.localDate
                )
                if (existing != null) {
                    val updated = holiday.copy(id = existing.id, updatedAt = LocalDateTime.now())
                    holidayRepository.save(updated)
                } else {
                    // 3. 없으면 새로 insert
                    holidayRepository.save(holiday)
                }
            }
        }
    }

}

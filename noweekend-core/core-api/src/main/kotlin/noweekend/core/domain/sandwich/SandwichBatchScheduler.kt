package noweekend.core.domain.sandwich

import noweekend.client.mcp.recommend.RecommendClient
import noweekend.client.mcp.recommend.model.SandwichRequest
import noweekend.core.domain.holiday.HolidayReader
import noweekend.core.domain.weekend.WeekendReader
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class SandwichBatchScheduler(
    private val sandwichWriter: SandwichWriter,
    private val recommendClient: RecommendClient,
    private val holidayReader: HolidayReader,
    private val weekendReader: WeekendReader,
) {
    @Scheduled(cron = "1 0 0 * * *")
    fun scheduledGenerate() {
        val searchDate = LocalDateTime.now().plusDays(1)
            .withMinute(0).withSecond(0).withNano(0)
        generateAndSaveSandwichRecommend(searchDate)
    }

    fun generateAndSaveSandwichRecommend(searchDate: LocalDateTime) {
        val holidays = holidayReader.findRemainingHolidays(searchDate.toLocalDate())
            .map { it.date }
        val weekends = weekendReader.getUpcomingWeekends()
            .map { it.date }

        val sandwichRequest = SandwichRequest(
            holidays = holidays,
            weekends = weekends,
        )

        val bridgePeriods = recommendClient.getSandwich(sandwichRequest)
        val holidayOrWeekendSet = holidays.toSet() + weekends.toSet()

        val caches = bridgePeriods.map { period ->
            val allDates = generateDateRange(period.startDate, period.endDate)
            val useAnnualLeaveDates = allDates.filter { date ->
                !holidayOrWeekendSet.contains(date) && date.dayOfWeek.value in 1..5
            }
            SandwichRecommendCache.register(
                startDate = period.startDate,
                endDate = period.endDate,
                searchDate = searchDate,
                useAnnualLeave = useAnnualLeaveDates.size,
                totalVacationDays = allDates.size,
            )
        }
        sandwichWriter.registerAll(caches)
    }

    private fun generateDateRange(start: LocalDate, end: LocalDate): List<LocalDate> {
        return (0..java.time.temporal.ChronoUnit.DAYS.between(start, end)).map { start.plusDays(it) }
    }
}

package noweekend.core.domain.sandwich

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Year
import java.time.temporal.ChronoUnit
import java.util.concurrent.ThreadLocalRandom

@Service
class SandwichCalculator {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun recommendSandwich(
        holidays: Set<LocalDate>,
        weekends: Set<LocalDate>,
        maxGap: Int = 2,
        minSpan: Int = 3,
        from: LocalDate = LocalDate.now(),
        until: LocalDate = Year.now().atMonth(12).atEndOfMonth(),
    ): List<Sandwich> {
        val candidates = buildCandidates(holidays, weekends, maxGap, from, until)

        val rawPeriods = candidates.mapNotNull { startDate ->
            // ↓ 블록 첫날이 아닌 특수일(공휴일/주말) 시작일은 스킵
            val specials = holidays + weekends
            if (startDate in specials && (startDate.minusDays(1) in specials)) {
                return@mapNotNull null
            }

            val period = extendPeriod(startDate, holidays, weekends, maxGap, until, candidates)
            val length = ChronoUnit.DAYS.between(period.startDate, period.endDate).toInt() + 1
            if (length < minSpan) return@mapNotNull null

            val firstHol = holidays
                .filter { it in period.startDate..period.endDate }
                .minOrNull() ?: return@mapNotNull null

            val needed = countRequiredLeaves(period.startDate, firstHol, holidays, weekends)
            if (needed > maxGap) return@mapNotNull null

            // 기존 “기간 내 갭(공휴·주말 아닌 날) 여부” 검사도 유지
            var d = period.startDate
            var hasGap = false
            while (!d.isAfter(period.endDate)) {
                if (d !in holidays && d !in weekends) {
                    hasGap = true
                    break
                }
                d = d.plusDays(1)
            }
            if (!hasGap) return@mapNotNull null

            period
        }
            .distinct()

        val response = rawPeriods
            .sortedBy { it.startDate }
            .fold(mutableListOf<MutableList<Sandwich>>()) { groups, period ->
                if (groups.isEmpty() ||
                    period.startDate.isAfter(groups.last().maxOf { it.endDate })
                ) {
                    groups += mutableListOf(period)
                } else {
                    groups.last() += period
                }
                groups
            }
            .map { group ->
                group[ThreadLocalRandom.current().nextInt(group.size)]
            }

        logger.info("=========================== HOLIDAY ===========================")
        holidays.forEach { holiday ->
            print(holiday)
            print(" ")
        }
        logger.info("")
        logger.info("=========================== HOLIDAY ===========================")

        logger.info("=========================== WEEKEND ===========================")
        weekends.forEach { weekend ->
            print(weekend)
            print(" ")
        }
        logger.info("")
        logger.info("=========================== WEEKEND ===========================")
        logger.info("")
        logger.info("=========================== rawPeriods ===========================")
        rawPeriods.forEach {
            print(it.startDate)
            print("     ~    ")
            logger.info(it.endDate.toString())
        }
        logger.info("=========================== rawPeriods ===========================")

        logger.info("=========================== response ===========================")
        response.forEach {
            print(it.startDate)
            print("     ~    ")
            logger.info(it.endDate.toString())
        }
        logger.info("=========================== response ===========================")

        return response
    }

    private fun buildCandidates(
        holidays: Set<LocalDate>,
        weekends: Set<LocalDate>,
        maxGap: Int,
        from: LocalDate,
        until: LocalDate,
    ): List<LocalDate> {
        val base = holidays + weekends
        val preHoliday = holidays.flatMap { hol ->
            (1..maxGap).mapNotNull { offset ->
                hol.minusDays(offset.toLong()).takeIf { it in from..until }
            }
        }
        return (base + preHoliday)
            .filter { it in from..until }
            .distinct()
            .sorted()
    }

    private fun extendPeriod(
        startDate: LocalDate,
        holidays: Set<LocalDate>,
        weekends: Set<LocalDate>,
        globalMaxGap: Int,
        until: LocalDate,
        specials: List<LocalDate>,
    ): Sandwich {
        val isPreGapStart = startDate !in holidays && startDate !in weekends

        var gapCount = 0
        var contigCount = 0
        var lastWasGap = false

        var current = startDate
        var endDate = startDate

        while (!current.isAfter(until)) {
            val isHoliday = current in holidays
            val isWeekend = current in weekends

            if (isHoliday || isWeekend) {
                // reset gap, increment contiguous holiday/weekend count
                contigCount = if (lastWasGap) 1 else contigCount + 1
                gapCount = 0
                lastWasGap = false
                endDate = current
            } else {
                if (!lastWasGap) gapCount = 0
                gapCount++

                val currentGapMax = when {
                    contigCount >= 3 && isPreGapStart -> 0
                    contigCount >= 3 -> 1
                    else -> globalMaxGap
                }
                if (gapCount > currentGapMax) break

                if (contigCount < 3) {
                    val nextSpecial = specials.firstOrNull { it > current }
                    val window = (currentGapMax - gapCount + 1).toLong()
                    if (nextSpecial == null || nextSpecial.isAfter(current.plusDays(window))) {
                        break
                    }
                }

                lastWasGap = true
                endDate = current
            }
            current = current.plusDays(1)
        }
        return Sandwich(startDate, endDate)
    }

    private fun countRequiredLeaves(
        startDate: LocalDate,
        firstHoliday: LocalDate,
        holidays: Set<LocalDate>,
        weekends: Set<LocalDate>,
    ): Int {
        var leaves = 0
        var day = startDate.plusDays(1)
        while (day.isBefore(firstHoliday)) {
            if (day !in holidays && day !in weekends) leaves++
            day = day.plusDays(1)
        }
        return leaves
    }
}

package noweekend.core.domain.sandwich

import noweekend.core.domain.holiday.HolidayReader
import noweekend.core.domain.weekend.WeekendReader
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.temporal.ChronoUnit
import java.util.concurrent.ThreadLocalRandom

@Service
class SandwichRecommendService(
    private val holidayReader: HolidayReader,
    private val weekendReader: WeekendReader,
) {

    data class VacationPeriod(val startDate: LocalDate, val endDate: LocalDate)

    fun generateAndSaveSandwichRecommend(searchDate: LocalDateTime) {
        val today = searchDate.toLocalDate()
        val holidays = holidayReader
            .findRemainingHolidays(today)
            .map { it.date }
            .toSet()

        val weekends = weekendReader
            .getAllThisYearWeekends()
            .map { it.date }
            .filter { it.isAfter(today) }
            .toSet()

        val until = Year.now().atMonth(12).atEndOfMonth()

        val periods = findSandwichVacations(
            holidays     = holidays,
            weekends     = weekends,
            globalMaxGap = 2,
            minSpan      = 3,
            from         = today,
            until        = until
        )

        println("=========================== HOLIDAY ===========================")
        holidays.forEach { holiday ->
            print(holiday)
            print(" ")
        }
        println()
        println("=========================== HOLIDAY ===========================")

        println("=========================== WEEKEND ===========================")
        weekends.forEach { weekend ->
            print(weekend)
            print(" ")
        }
        println()
        println("=========================== WEEKEND ===========================")
        periods.forEach {
            print(it.startDate)
            print("     ~    ")
            println(it.endDate)
        }
    }

    private fun findSandwichVacations(
        holidays: Set<LocalDate>,
        weekends: Set<LocalDate>,
        globalMaxGap: Int,
        minSpan: Int,
        from: LocalDate,
        until: LocalDate
    ): List<VacationPeriod> {
        // 1) 공휴일·주말을 기본 후보로
        val baseCandidates = (holidays + weekends)

        // 2) 공휴일 전에 최대 globalMaxGap 일까지, gap 후보로 추가
        val gapCandidates = holidays.flatMap { hol ->
            (1..globalMaxGap).mapNotNull { offset ->
                val d = hol.minusDays(offset.toLong())
                if (d in from..until) d else null
            }
        }

        // 3) 후보 합치고 정렬
        val candidates = (baseCandidates + gapCandidates)
            .distinct()
            .filter { it in from..until }
            .sorted()

        // 4) 모든 블럭(rawPeriods) 생성
        val rawPeriods = candidates.mapNotNull { start ->
            val block = extendVacation(start, holidays, weekends, globalMaxGap, until, candidates)
            val span = ChronoUnit.DAYS.between(block.startDate, block.endDate).toInt() + 1
            if (span < minSpan) return@mapNotNull null

            // 블럭에 첫 공휴일까지 필요한 연차 계산
            val firstHol = holidays
                .filter { it in block.startDate..block.endDate }
                .minOrNull()
                ?: return@mapNotNull null

            val needed = countRequiredLeaves(block.startDate, firstHol, holidays, weekends)
            if (needed > globalMaxGap) return@mapNotNull null

            block
        }
            .distinct()

        // 5) 겹치는 블럭끼리 그룹핑
        val groups = mutableListOf<MutableList<VacationPeriod>>()
        rawPeriods
            .sortedBy { it.startDate }
            .forEach { period ->
                if (groups.isEmpty() ||
                    period.startDate.isAfter(groups.last().maxOf { it.endDate })
                ) {
                    // 새 그룹 시작
                    groups += mutableListOf(period)
                } else {
                    // 마지막 그룹에 추가
                    groups.last() += period
                }
            }

        // 6) 그룹별로 랜덤 하나씩 선택해 반환
        return groups.map { group ->
            val idx = ThreadLocalRandom.current().nextInt(group.size)
            group[idx]
        }
    }

    private fun extendVacation(
        startDate: LocalDate,
        holidays: Set<LocalDate>,
        weekends: Set<LocalDate>,
        globalMaxGap: Int,
        until: LocalDate,
        specials: List<LocalDate>
    ): VacationPeriod {
        // pre-gap start 인지 체크 (공휴일도 주말도 아닌 날짜에서 시작했으면 true)
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
                // 공휴일·주말이면 연속 카운트↑, gap 리셋
                contigCount = if (lastWasGap) 1 else contigCount + 1
                gapCount = 0
                lastWasGap = false
                endDate = current

            } else {
                // gap 처리
                if (!lastWasGap) gapCount = 0
                gapCount++

                // contigCount >= 3 이면서 pre-gap start 면 후방 gap 허용 0일
                val currentGapMax = when {
                    contigCount >= 3 && isPreGapStart -> 0
                    contigCount >= 3                  -> 1
                    else                              -> globalMaxGap
                }

                if (gapCount > currentGapMax) break

                // lookahead: 순수 주말만 contigCount<3 일 때만 적용
                if (contigCount < 3) {
                    val nextSpecial = specials.firstOrNull { it > current }
                    val window      = (currentGapMax - gapCount + 1).toLong()
                    if (nextSpecial == null || nextSpecial.isAfter(current.plusDays(window))) {
                        break
                    }
                }

                lastWasGap = true
                endDate     = current
            }

            current = current.plusDays(1)
        }

        return VacationPeriod(startDate, endDate)
    }

    private fun countRequiredLeaves(
        startDate: LocalDate,
        firstHoliday: LocalDate,
        holidays: Set<LocalDate>,
        weekends: Set<LocalDate>
    ): Int {
        var leaves = 0
        var d = startDate.plusDays(1)
        while (d.isBefore(firstHoliday)) {
            if (d !in holidays && d !in weekends) leaves++
            d = d.plusDays(1)
        }
        return leaves
    }
}

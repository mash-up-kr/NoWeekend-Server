package noweekend.core.domain.recommend

import noweekend.client.mcp.recommend.RecommendClient
import noweekend.client.mcp.recommend.model.AiGenerateVacationRequest
import noweekend.client.mcp.recommend.model.WeatherRequest
import noweekend.core.api.controller.v1.request.GenerateVacationRequest
import noweekend.core.api.controller.v1.response.AiVacationApiResponse
import noweekend.core.api.controller.v1.response.SandwichApiResponse
import noweekend.core.api.controller.v1.response.SandwichResponse
import noweekend.core.api.controller.v1.response.WeatherResponse
import noweekend.core.domain.ActivityType
import noweekend.core.domain.LeisurePreference
import noweekend.core.domain.RestPreference
import noweekend.core.domain.TravelStyle
import noweekend.core.domain.holiday.HolidayReader
import noweekend.core.domain.sandwich.Sandwich
import noweekend.core.domain.sandwich.SandwichCalculator
import noweekend.core.domain.tag.RecommendType
import noweekend.core.domain.tag.TagReader
import noweekend.core.domain.tag.TagRecommendCache
import noweekend.core.domain.tag.TagRecommendCacheReader
import noweekend.core.domain.tag.TagRecommendCacheWriter
import noweekend.core.domain.tag.TagRecommendation
import noweekend.core.domain.tag.TagRecommendations
import noweekend.core.domain.tag.UserTags
import noweekend.core.domain.user.Location
import noweekend.core.domain.user.UserReader
import noweekend.core.domain.vacation.AiVacation
import noweekend.core.domain.vacation.AiVacationReader
import noweekend.core.domain.vacation.AiVacationWriter
import noweekend.core.domain.vacation.IconStyle
import noweekend.core.domain.weather.WeatherReader
import noweekend.core.domain.weather.WeatherRecommendCache
import noweekend.core.domain.weather.WeatherRecommendation
import noweekend.core.domain.weather.WeatherWriter
import noweekend.core.domain.weekend.WeekendReader
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Year
import java.time.temporal.ChronoUnit
import kotlin.random.Random

@Service
class RecommendServiceImpl(
    private val recommendClient: RecommendClient,
    private val userReader: UserReader,
    private val tagReader: TagReader,
    private val holidayReader: HolidayReader,
    private val weatherReader: WeatherReader,
    private val weatherWriter: WeatherWriter,
    private val tagRecommendCacheReader: TagRecommendCacheReader,
    private val tagRecommendCacheWriter: TagRecommendCacheWriter,
    private val weekendReader: WeekendReader,
    private val calculator: SandwichCalculator,

    private val aiVacationReader: AiVacationReader,
    private val aiVacationWriter: AiVacationWriter,
) : RecommendService {

    override fun getWeatherRecommend(userId: String): WeatherResponse {
        val location = getUserLocation(userId)
        val today = LocalDate.now()
        val cached = getCachedWeather(location, today)
        if (cached != null) {
            return WeatherResponse(cached.weatherResponses)
        }
        try {
            val apiResponse = fetchWeatherFromApi(location)
            saveWeatherCache(location, today, apiResponse)
            return WeatherResponse(apiResponse)
        } catch (_: Exception) {
            throw CoreException(ErrorType.MCP_SERVER_WEATHER_ERROR)
        }
    }

    private fun getUserLocation(userId: String): Location = (
        userReader.findLocationByUserId(userId)
            ?: throw CoreException(ErrorType.USER_LOCATION_NOT_FOUND)
        )

    private fun getCachedWeather(location: Location, today: LocalDate): WeatherRecommendCache? {
        return weatherReader.findCacheByLocationAndDate(
            latitude = location.latitude,
            longitude = location.longitude,
            searchDate = today,
        )
    }

    private fun fetchWeatherFromApi(location: Location): List<WeatherRecommendation> {
        val withinKorea = isWithinKorea(location.latitude, location.longitude)
        if (!withinKorea) {
            throw CoreException(ErrorType.INVALID_LOCATION)
        }

        val request = WeatherRequest(location.longitude, location.latitude)
        try {
            return recommendClient.getFutureWeather(request)
        } catch (_: Exception) {
            throw CoreException(ErrorType.MCP_SERVER_WEATHER_ERROR)
        }
    }

    private fun isWithinKorea(lat: Double, lon: Double): Boolean {
        return lat in 33.1..38.6 && lon in 124.65..130.93
    }

    private fun saveWeatherCache(
        location: Location,
        today: LocalDate,
        apiResponse: List<WeatherRecommendation>,
    ) {
        val cacheObj = WeatherRecommendCache.register(
            latitude = location.latitude,
            longitude = location.longitude,
            searchDate = today,
            weatherResponses = apiResponse,
        )
        weatherWriter.register(cacheObj)
    }

    override fun getTagRecommend(userId: String): TagRecommendations {
        val userTags = tagReader.getUserTags(userId)
        userTagValidation(userTags)
        val cached = getTagRecommendCaching(RecommendType.MIXED, userId)

        if (cached != null) {
            return cached.recommend
        }

        val apiRecommendResponse = recommendClient.getRecommend(userTags)
        if (apiRecommendResponse != null) {
            registerTagRecommendCache(
                type = RecommendType.MIXED,
                userTags = userTags,
                apiResponse = apiRecommendResponse,
                userId = userId,
            )
            return apiRecommendResponse
        }

        return mcpClientNotResponding(userTags)
    }

    private fun userTagValidation(userTags: UserTags) {
        val userFlatMapTags = userTags.selectedBasicTags + userTags.selectedCustomTags
        if (userFlatMapTags.size < 3) {
            throw CoreException(ErrorType.USER_TAGS_ERROR)
        }
    }

    private fun getTagRecommendCaching(tagRecommendType: RecommendType, userId: String): TagRecommendCache? {
        return tagRecommendCacheReader.findTodayCache(
            recommendType = tagRecommendType,
            userId = userId,
        )
    }

    private fun registerTagRecommendCache(
        type: RecommendType,
        userTags: UserTags,
        apiResponse: TagRecommendations,
        userId: String,
    ) {
        val cache = TagRecommendCache.register(
            recommendType = type,
            searchDate = LocalDate.now(),
            tags = userTags,
            recommend = apiResponse,
            userId = userId,
        )
        tagRecommendCacheWriter.register(cache)
    }

    private fun mcpClientNotResponding(userTags: UserTags): TagRecommendations {
        val selectedTags = userTags.selectedBasicTags + userTags.selectedCustomTags
        val shuffled = selectedTags.shuffled(Random(System.currentTimeMillis()))

        return TagRecommendations(
            firstRecommendTag = TagRecommendation(shuffled[0].content),
            secondRecommendTag = TagRecommendation(shuffled[1].content),
            thirdRecommendTag = TagRecommendation(shuffled[2].content),
        )
    }

    override fun getTagRecommendOnlyNew(userId: String): TagRecommendations {
        val userTags = tagReader.getUserTags(userId)
        userTagValidation(userTags)

        val cached = getTagRecommendCaching(RecommendType.ONLY_NEW, userId)
        if (cached != null) {
            return cached.recommend
        }

        val apiRecommendResponse = recommendClient.getOnlyNewRecommend(userTags)
        if (apiRecommendResponse != null) {
            val allOldTags = (
                userTags.selectedBasicTags + userTags.unselectedBasicTags +
                    userTags.selectedCustomTags + userTags.unselectedCustomTags
                )
                .map { it.content }
                .toSet()

            val allNewTags = listOf(
                apiRecommendResponse.firstRecommendTag.content,
                apiRecommendResponse.secondRecommendTag.content,
                apiRecommendResponse.thirdRecommendTag.content,
            )
            require(allNewTags.none { it in allOldTags }) { "Returned tag already exists in user tags" }
            registerTagRecommendCache(RecommendType.ONLY_NEW, userTags, apiRecommendResponse, userId)
            return apiRecommendResponse
        }

        throw CoreException(ErrorType.MCP_SERVER_TAGS_ERROR)
    }

    override fun getSandwich(): SandwichApiResponse {
        // 1) 기준 날짜
        val today = LocalDate.now()

        // 2) 남은 공휴일, 주말 조회
        val holidays = holidayReader.findRemainingHolidays(today).map { it.date }.toSet()
        val weekends = weekendReader.getAllThisYearWeekends()
            .map { it.date }
            .filter { it.isAfter(today) }
            .toSet()

        // 3) 연말까지 계산
        val until = Year.now().atMonth(12).atEndOfMonth()
        val periods = calculator.recommendSandwich(
            holidays = holidays,
            weekends = weekends,
            maxGap = 2,
            minSpan = 3,
            from = today,
            until = until,
        )

        // 4) VacationPeriod → SandwichResponse 매핑
        val responses = periods.map { period ->
            val totalDays = ChronoUnit.DAYS.between(period.startDate, period.endDate).toInt() + 1
            val useAnnualLeave = generateSequence(period.startDate) { it.plusDays(1) }
                .takeWhile { !it.isAfter(period.endDate) }
                .count { date -> date !in holidays && date !in weekends }
            SandwichResponse(
                startDate = period.startDate,
                endDate = period.endDate,
                useAnnualLeave = useAnnualLeave,
                totalVacationDays = totalDays,
            )
        }

        return SandwichApiResponse(responses = responses)
    }

    override fun getVacation(userId: String): AiVacationApiResponse {
        val cache = aiVacationReader.findByUserIdAndSearchDate(userId, LocalDate.now())
        if (cache != null) {
            return AiVacationApiResponse(
                title = cache.title,
                content = cache.content,
                startDate = cache.startDate,
                endDate = cache.endDate,
                iconStyle = cache.iconStyle,
            )
        }

        throw CoreException(ErrorType.VACATION_NOT_FOUND)
    }

    @Async
    override fun generateVacation(userId: String, request: GenerateVacationRequest) {
        val tags = tagReader.getUserTags(userId)
        val selected = (tags.selectedBasicTags + tags.selectedCustomTags).map { it.content }
        val unselected = (tags.unselectedBasicTags + tags.unselectedCustomTags).map { it.content }

        val travelStyleLabels = TravelStyle.entries.map { it.korean }
        val activityTypeLabels = ActivityType.entries.map { it.korean }
        val restPreferenceLabels = RestPreference.entries.map { it.korean }
        val leisurePrefLabels = LeisurePreference.entries.map { it.korean }

        val periods = getSandwichLocalDates(LocalDate.now())

        val startDate = startDate(periods, request.days.toLong())
        val endDate = endDate(periods, request.days.toLong())

        val aiRequest = AiGenerateVacationRequest(
            travelStyleOptionLabels = travelStyleLabels,
            chosenTravelStyleLabel = request.travelStyle.korean,

            activityTypeOptionLabels = activityTypeLabels,
            chosenActivityTypeLabel = request.activityType.korean,

            restPreferenceOptionLabels = restPreferenceLabels,
            chosenRestPreferenceLabel = request.restPreference.korean,

            leisurePreferenceOptionLabels = leisurePrefLabels,
            chosenLeisurePreferenceLabel = request.leisurePreference.korean,

            selectedTags = selected,
            unselectedTags = unselected,
            startDate = startDate,
            endDate = endDate,
        )

        val iconStyle = solveIcon(request)
        val aiResponse =
            recommendClient.generateVacation(aiRequest) ?: throw CoreException(ErrorType.MCP_SERVER_INTERNAL_ERROR)

        aiVacationWriter.register(
            AiVacation.register(
                title = aiResponse.title,
                content = aiResponse.content,
                iconStyle = iconStyle,
                searchDate = LocalDate.now(),
                startDate = aiResponse.startDate,
                endDate = endDate,
                userId = userId,
            ),
        )
    }

    fun startDate(periods: List<Sandwich>, days: Long): LocalDate {
        return if (periods.isEmpty()) {
            LocalDate.now().plusMonths(1).plusDays(days)
        } else {
            periods[0].startDate
        }
    }

    fun endDate(periods: List<Sandwich>, days: Long): LocalDate {
        return if (periods.isEmpty()) {
            LocalDate.now().plusDays(days)
        } else {
            periods[0].endDate
        }
    }

    private fun solveIcon(request: GenerateVacationRequest): IconStyle {
        if (request.activityType == ActivityType.AT_HOME) {
            return IconStyle.HOUSE
        }

        if (request.activityType == ActivityType.OUTDOOR) {
            if (request.travelStyle == TravelStyle.PLANNER) {
                return IconStyle.PLANE
            }

            if (request.travelStyle == TravelStyle.SPONTANEOUS) {
                return IconStyle.TRAIN
            }
        }

        return IconStyle.STAR
    }

    fun getSandwichLocalDates(today: LocalDate): List<Sandwich> {
        val holidays = holidayReader.findRemainingHolidays(today).map { it.date }.toSet()
        val weekends = weekendReader.getAllThisYearWeekends()
            .map { it.date }
            .filter { it.isAfter(today) }
            .toSet()

        return calculator.recommendSandwich(
            holidays = holidays,
            weekends = weekends,
            maxGap = 2,
            minSpan = 3,
            from = today,
            until = Year.now().atMonth(12).atEndOfMonth(),
        )
    }
}

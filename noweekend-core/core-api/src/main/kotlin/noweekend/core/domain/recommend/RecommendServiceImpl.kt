package noweekend.core.domain.recommend

import noweekend.client.mcp.McpNotRespondingException
import noweekend.client.mcp.recommend.RecommendClient
import noweekend.client.mcp.recommend.model.AiGenerateVacationRequest
import noweekend.client.mcp.recommend.model.SandwichApiResponse
import noweekend.client.mcp.recommend.model.SandwichRequest
import noweekend.client.mcp.recommend.model.SandwichResponse
import noweekend.client.mcp.recommend.model.WeatherRequest
import noweekend.core.api.controller.v1.request.GenerateVacationRequest
import noweekend.core.api.controller.v1.response.AiGenerateVacationApiResponse
import noweekend.core.api.controller.v1.response.WeatherResponse
import noweekend.core.domain.ActivityType
import noweekend.core.domain.IconStyle
import noweekend.core.domain.LeisurePreference
import noweekend.core.domain.RestPreference
import noweekend.core.domain.TravelStyle
import noweekend.core.domain.holiday.Holiday
import noweekend.core.domain.holiday.HolidayReader
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
import noweekend.core.domain.weather.WeatherReader
import noweekend.core.domain.weather.WeatherRecommendCache
import noweekend.core.domain.weather.WeatherRecommendation
import noweekend.core.domain.weather.WeatherWriter
import noweekend.core.domain.weekend.WeekendReader
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType
import org.springframework.stereotype.Service
import java.time.LocalDate
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

    override fun getSandwich(userId: String): SandwichApiResponse {
        val findUser = userReader.findUserById(userId) ?: throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL)
        val birthDate = findUser.birthDate ?: throw CoreException(ErrorType.USER_BIRTH_DAY_NOT_FOUND)
        val holidays: List<LocalDate> = holidayReader.findRemainingHolidays(LocalDate.now())
            .map { holiday: Holiday -> holiday.date }

        val weekends: List<LocalDate> = weekendReader.getUpcomingWeekends().map { weekend -> weekend.date }

        val holidayOrWeekendSet = holidays.toSet() + weekends.toSet()
        val remainingAnnualLeave = findUser.remainingAnnualLeave ?: throw CoreException(ErrorType.INVALID_LOCATION)

        try {
            val bridgePeriods = recommendClient.getSandwich(
                SandwichRequest(birthDay = birthDate, holidays = holidays, remainingAnnualLeave.toInt(), weekends),
            )
            return SandwichApiResponse(
                bridgePeriods.map { period ->
                    val allDates = generateDateRange(period.startDate, period.endDate)
                    val useAnnualLeaveDates = allDates.filter { date ->
                        !holidayOrWeekendSet.contains(date) && date.dayOfWeek.value in 1..5
                    }
                    SandwichResponse(
                        startDate = period.startDate,
                        endDate = period.endDate,
                        useAnnualLeave = useAnnualLeaveDates.size,
                        totalVacationDays = allDates.size,
                    )
                }.toList(),
            )
        } catch (_: McpNotRespondingException) {
            throw CoreException(ErrorType.MCP_SERVER_INTERNAL_ERROR)
        }
    }

    fun generateDateRange(start: LocalDate, end: LocalDate): List<LocalDate> {
        return (0..ChronoUnit.DAYS.between(start, end)).map { start.plusDays(it) }
    }

    override fun generateVacation(userId: String, request: GenerateVacationRequest): AiGenerateVacationApiResponse {
        val user = userReader.findUserById(userId) ?: throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL)
        val birthDate = user.birthDate ?: throw CoreException(ErrorType.USER_BIRTH_DAY_NOT_FOUND)

        val tags = tagReader.getUserTags(userId)
        val selected = (tags.selectedBasicTags + tags.selectedCustomTags).map { it.content }
        val unselected = (tags.unselectedBasicTags + tags.unselectedCustomTags).map { it.content }

        val today = LocalDate.now()
        val endDate = today.plusDays(15)
        val holidaysY = holidayReader.findAllByYear(today.year)
        val upcomingH = holidaysY
            .filter { it.date in today..endDate }
            .map { "${it.date}(${it.dayOfWeekKor.display})" }

        val travelStyleLabels = TravelStyle.entries.map { it.korean }
        val activityTypeLabels = ActivityType.entries.map { it.korean }
        val restPreferenceLabels = RestPreference.entries.map { it.korean }
        val leisurePrefLabels = LeisurePreference.entries.map { it.korean }

        val aiRequest = AiGenerateVacationRequest(
            days = request.days,
            travelStyleOptionLabels = travelStyleLabels,
            chosenTravelStyleLabel = request.travelStyle.korean,

            activityTypeOptionLabels = activityTypeLabels,
            chosenActivityTypeLabel = request.activityType.korean,

            restPreferenceOptionLabels = restPreferenceLabels,
            chosenRestPreferenceLabel = request.restPreference.korean,

            leisurePreferenceOptionLabels = leisurePrefLabels,
            chosenLeisurePreferenceLabel = request.leisurePreference.korean,

            birthDate = birthDate,
            selectedTags = selected,
            unselectedTags = unselected,
            upcomingHolidays = upcomingH,
        )
        val iconStyle = solveIcon(request)
        val aiResponse =
            recommendClient.generateVacation(aiRequest) ?: throw CoreException(ErrorType.MCP_SERVER_INTERNAL_ERROR)
        return AiGenerateVacationApiResponse(
            title = aiResponse.title,
            content = aiResponse.content,
            iconStyle = iconStyle,
        )
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
}

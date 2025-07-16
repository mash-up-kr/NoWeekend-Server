package noweekend.core.domain.recommend

import com.fasterxml.jackson.databind.ObjectMapper
import noweekend.client.mcp.recommend.RecommendClient
import noweekend.client.mcp.recommend.model.SandwichRequest
import noweekend.client.mcp.recommend.model.SandwichResponse
import noweekend.client.mcp.recommend.model.WeatherRequest
import noweekend.core.api.controller.v1.response.WeatherResponse
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
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType
import org.springframework.stereotype.Service
import java.time.LocalDate
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
    private val objectMapper: ObjectMapper,
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
        val cached = getTagRecommendCaching(RecommendType.MIXED, userTags)

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

    private fun getTagRecommendCaching(tagRecommendType: RecommendType, userTags: UserTags): TagRecommendCache? {
        val tagsJson = objectMapper.writeValueAsString(userTags)
        return tagRecommendCacheReader.findTodayCache(
            recommendType = tagRecommendType,
            tagsJson = tagsJson,
            searchDate = LocalDate.now(),
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

        val cached = getTagRecommendCaching(RecommendType.ONLY_NEW, userTags)
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

    override fun getSandwich(userId: String): SandwichResponse {
        val findUser = userReader.findUserById(userId) ?: throw CoreException(ErrorType.USER_NOT_FOUND_INTERNAL)
        val birthDate = findUser.birthDate ?: throw CoreException(ErrorType.USER_BIRTH_DAY_NOT_FOUND)
        val holidays: List<LocalDate> = holidayReader
            .findAllByYear(LocalDate.now().year)
            .map { it.date }

        return recommendClient.getSandwich(
            SandwichRequest(birthDay = birthDate, holidays = holidays),
        ) ?: throw CoreException(ErrorType.MCP_SERVER_SANDWICH_ERROR)
    }
}

package noweekend.core.domain.recommend

import noweekend.client.mcp.recommend.RecommendClient
import noweekend.client.mcp.recommend.model.SandwichRequest
import noweekend.client.mcp.recommend.model.SandwichResponse
import noweekend.client.mcp.recommend.model.TagApiResponses
import noweekend.client.mcp.recommend.model.TagResponse
import noweekend.client.mcp.recommend.model.WeatherRequest
import noweekend.core.api.controller.v1.response.WeatherApiResponse
import noweekend.core.domain.holiday.HolidayReader
import noweekend.core.domain.tag.TagReader
import noweekend.core.domain.tag.UserTags
import noweekend.core.domain.user.UserReader
import noweekend.core.domain.weather.WeatherReader
import noweekend.core.domain.weather.WeatherRecommendCache
import noweekend.core.domain.weather.WeatherWriter
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType
import org.slf4j.LoggerFactory
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
) : RecommendService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getWeatherRecommend(userId: String): WeatherApiResponse {
        val location = userReader.findLocationByUserId(userId)
            ?: throw CoreException(ErrorType.USER_LOCATION_NOT_FOUND)

        val today = LocalDate.now()
        // 1. 캐시에서 먼저 조회
        val cached = weatherReader.findCacheByLocationAndDate(
            latitude = location.latitude,
            longitude = location.longitude,
            searchDate = today,
        )
        if (cached != null) {
            return WeatherApiResponse(cached.weatherResponses)
        }

        val apiResponse = recommendClient.getFutureWeather(
            WeatherRequest(
                longitude = location.longitude,
                latitude = location.latitude,
            ),
        )

        log.info("======================================== spi 호출 시작")
        log.info("apiResponse = ${apiResponse.size}\n")
        apiResponse.forEach { response -> log.info(response.toString() + "\n") }
        log.info("======================================== spi 호출 종료")

        val cacheObj = WeatherRecommendCache.register(
            latitude = location.latitude,
            longitude = location.longitude,
            searchDate = today,
            weatherResponses = apiResponse,
        )
        weatherWriter.register(cacheObj)

        return WeatherApiResponse(apiResponse)
    }

    override fun getTagRecommend(userId: String): TagApiResponses {
        val userTags = tagReader.getUserTags(userId)
        userTagValidation(userTags)
        // ToDo 오늘 태그를 추천을 받았다면 받았던 걸로 반환하는 로직 추가

        val apiRecommendResponse = recommendClient.getRecommend(userTags)
        if (apiRecommendResponse != null) {
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

    private fun mcpClientNotResponding(userTags: UserTags): TagApiResponses {
        val selectedTags = userTags.selectedBasicTags + userTags.selectedCustomTags
        val shuffled = selectedTags.shuffled(Random(System.currentTimeMillis()))

        return TagApiResponses(
            firstRecommendTag = TagResponse(shuffled[0].content),
            secondRecommendTag = TagResponse(shuffled[1].content),
            thirdRecommendTag = TagResponse(shuffled[2].content),
        )
    }

    override fun getTagRecommendOnlyNew(userId: String): TagApiResponses {
        val userTags = tagReader.getUserTags(userId)
        userTagValidation(userTags)

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

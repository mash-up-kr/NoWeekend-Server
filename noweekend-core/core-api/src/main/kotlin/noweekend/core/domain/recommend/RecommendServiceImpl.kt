package noweekend.core.domain.recommend

import noweekend.client.weather.WeatherRecommendClient
import noweekend.client.weather.model.WeatherRequest
import noweekend.core.api.controller.v1.response.WeatherApiResponse
import noweekend.core.domain.user.UserReader
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType
import org.springframework.stereotype.Service

@Service
class RecommendServiceImpl(
    private val weatherRecommendClient: WeatherRecommendClient,
    private val userReader: UserReader,
) : RecommendService {

    override fun getWeatherRecommend(userId: String): WeatherApiResponse {
        val location =
            userReader.findLocationByUserId(userId) ?: throw CoreException(ErrorType.USER_LOCATION_NOT_FOUND)

        val recommendWeathers = weatherRecommendClient.getFutureWeather(
            WeatherRequest(
                longitude = location.longitude,
                latitude = location.latitude,
            ),
        )

        return WeatherApiResponse(recommendWeathers)
    }
}

package noweekend.client.weather

import noweekend.client.weather.model.WeatherRequest
import noweekend.client.weather.model.WeatherResponse
import org.springframework.stereotype.Component

@Component
class WeatherRecommendClient(
    private val api: WeatherRecommendApi,
) {
    fun getFutureWeather(request: WeatherRequest): List<WeatherResponse> {
        return api.getFutureWeather(request).body ?: emptyList()
    }

    /*
    fun getFutureWeather(request: WeatherRequest): List<WeatherResponse> {
        try {
            val response = api.getFutureWeather(request)
            // HTTP status가 2xx가 아닐 때도 예외 던질 수 있음
            return response.body ?: emptyList()
        } catch (e: FeignException) {
            // 필요하다면 로그도 남기기
            throw CoreException(ErrorType.WEATHER_SERVER_ERROR, e.message)
        } catch (e: Exception) {
            throw CoreException(ErrorType.WEATHER_SERVER_UNKNOWN_ERROR, e.message)
        }
    }
     */
}

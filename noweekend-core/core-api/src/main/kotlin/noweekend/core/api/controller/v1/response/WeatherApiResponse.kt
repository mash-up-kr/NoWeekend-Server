package noweekend.core.api.controller.v1.response

import noweekend.core.domain.weather.WeatherRecommendation

data class WeatherApiResponse(
    val weatherRespons: List<WeatherRecommendation>,
)

package noweekend.core.api.controller.v1.response

import noweekend.core.domain.weather.WeatherRecommendation

data class WeatherResponse(
    val weatherResponses: List<WeatherRecommendation>,
)

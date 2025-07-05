package noweekend.core.api.controller.v1.response

import noweekend.client.weather.model.WeatherResponse

data class WeatherApiResponse(
    val weatherResponses: List<WeatherResponse>,
)

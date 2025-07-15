package noweekend.core.api.controller.v1.response

import noweekend.core.domain.IconStyle

data class AiGenerateVacationApiResponse(
    val title: String,
    val content: String,
    val iconStyle: IconStyle,
)

package noweekend.client.mcp.recommend.model

import java.time.LocalDate

data class AiGenerateVacationRequest(
    val startDate: LocalDate,
    val endDate: LocalDate,

    val travelStyleOptionLabels: List<String>,
    val chosenTravelStyleLabel: String,

    val activityTypeOptionLabels: List<String>,
    val chosenActivityTypeLabel: String,

    val restPreferenceOptionLabels: List<String>,
    val chosenRestPreferenceLabel: String,

    val leisurePreferenceOptionLabels: List<String>,
    val chosenLeisurePreferenceLabel: String,

    val selectedTags: List<String>,
    val unselectedTags: List<String>,
)

data class AiVacationResponse(
    val title: String,
    val content: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
)

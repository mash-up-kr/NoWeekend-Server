package noweekend.client.mcp.recommend.model

import java.time.LocalDate

data class AiGenerateVacationRequest(
    val days: Int,

    val travelStyleOptionLabels: List<String>,
    val chosenTravelStyleLabel: String,

    val activityTypeOptionLabels: List<String>,
    val chosenActivityTypeLabel: String,

    val restPreferenceOptionLabels: List<String>,
    val chosenRestPreferenceLabel: String,

    val leisurePreferenceOptionLabels: List<String>,
    val chosenLeisurePreferenceLabel: String,

    val birthDate: LocalDate,
    val selectedTags: List<String>,
    val unselectedTags: List<String>,
    val upcomingHolidays: List<String>,
)

data class AiGenerateVacationResponse(
    val title: String,
    val content: String,
)

package noweekend.core.domain.tag

data class TagRecommendation(
    val content: String,
)

data class TagRecommendations(
    val firstRecommendTag: TagRecommendation,
    val secondRecommendTag: TagRecommendation,
    val thirdRecommendTag: TagRecommendation,
)

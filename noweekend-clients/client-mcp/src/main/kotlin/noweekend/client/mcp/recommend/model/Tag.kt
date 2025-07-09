package noweekend.client.mcp.recommend.model

import noweekend.core.domain.tag.UserTags

data class TagRequest(
    val userTag: UserTags,
)

data class TagResponse(val content: String)

data class TagApiResponses(
    val firstRecommendTag: TagResponse,
    val secondRecommendTag: TagResponse,
    val thirdRecommendTag: TagResponse,
)

package noweekend.client.mcp.recommend.model

import noweekend.core.domain.tag.Tag
import noweekend.core.domain.tag.UserTags

data class TagRequest(
    val userTag: UserTagsForRequest,
)

data class UserTagsForRequest(
    val selectedBasicTags: List<SimpleTag>,
    val unselectedBasicTags: List<SimpleTag>,
    val selectedCustomTags: List<SimpleTag>,
    val unselectedCustomTags: List<SimpleTag>,
)

fun UserTags.toRequestType(): UserTagsForRequest {
    fun List<Tag>.toSimple() = map { SimpleTag(it.content) }
    return UserTagsForRequest(
        selectedBasicTags = selectedBasicTags.toSimple(),
        unselectedBasicTags = unselectedBasicTags.toSimple(),
        selectedCustomTags = selectedCustomTags.toSimple(),
        unselectedCustomTags = unselectedCustomTags.toSimple(),
    )
}

data class SimpleTag(
    val content: String,
)

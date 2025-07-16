package noweekend.core.domain.tag

data class UserTags(
    val selectedBasicTags: List<Tag>,
    val unselectedBasicTags: List<Tag>,
    val selectedCustomTags: List<Tag>,
    val unselectedCustomTags: List<Tag>,
)

data class ContentOnlyTag(val content: String)

fun UserTags.toContentOnly(): Map<String, List<ContentOnlyTag>> =
    mapOf(
        "selectedBasicTags" to selectedBasicTags.map { ContentOnlyTag(it.content) },
        "unselectedBasicTags" to unselectedBasicTags.map { ContentOnlyTag(it.content) },
        "selectedCustomTags" to selectedCustomTags.map { ContentOnlyTag(it.content) },
        "unselectedCustomTags" to unselectedCustomTags.map { ContentOnlyTag(it.content) },
    )

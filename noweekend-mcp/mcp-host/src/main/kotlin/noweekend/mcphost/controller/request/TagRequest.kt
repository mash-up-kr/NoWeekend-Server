package noweekend.mcphost.controller.request

data class TagRequest(
    val userTag: UserTags,
)

data class UserTags(
    val selectedBasicTags: List<Tag>,
    val unselectedBasicTags: List<Tag>,
    val selectedCustomTags: List<Tag>,
    val unselectedCustomTags: List<Tag>,
)

data class Tag(
    val content: String,
)

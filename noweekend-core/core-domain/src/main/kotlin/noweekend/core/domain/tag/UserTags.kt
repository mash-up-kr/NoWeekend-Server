package noweekend.core.domain.tag

data class UserTags(
    val selectedBasicTags: List<Tag>,
    val unselectedBasicTags: List<Tag>,
    val selectedCustomTags: List<Tag>,
    val unselectedCustomTags: List<Tag>,
)

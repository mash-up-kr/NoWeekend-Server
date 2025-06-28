package noweekend.core.domain.schedule

data class UserTags(
    val selectedBasicTags: List<Tag>,
    val unselectedBasicTags: List<Tag>,
    val selectedCustomTags: List<Tag>,
    val unselectedCustomTags: List<Tag>,
)

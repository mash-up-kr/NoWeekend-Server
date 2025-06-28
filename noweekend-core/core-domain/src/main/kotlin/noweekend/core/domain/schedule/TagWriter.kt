package noweekend.core.domain.schedule

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class TagWriter(
    private val tagRepository: TagRepository,
) {
    fun registerSelectedBasicTag(tags: List<BasicTag>, userId: String) {
        tags.stream().forEach {
                tag ->
            tagRepository.register(
                Tag.register(tag.koreanContent, userId),
            )
        }
    }

    fun upsertTags(
        addScheduleTags: List<String>,
        deleteScheduleTags: List<String>,
        userTags: UserTags,
        userId: String,
    ) {
        val allTags = userTags.selectedBasicTags +
            userTags.unselectedBasicTags +
            userTags.selectedCustomTags +
            userTags.unselectedCustomTags
        registerTags(addScheduleTags, allTags, userId)
        deleteTags(deleteScheduleTags, allTags)
    }

    private fun deleteTags(
        deleteScheduleTags: List<String>,
        allTags: List<Tag>,
    ) {
        allTags
            .filter { it.content in deleteScheduleTags && it.selected }
            .forEach { tagRepository.register(it.copy(selected = false)) }
    }

    private fun registerTags(
        addScheduleTags: List<String>,
        allTags: List<Tag>,
        userId: String,
    ) {
        addScheduleTags.forEach { addContent ->
            val existTag = allTags.find { it.content == addContent }

            // 존재하지 않으면 새로 생성
            if (existTag == null) {
                tagRepository.register(Tag.register(content = addContent, userId = userId))
                return@forEach // 다음 반복
            }

            // 존재하지만 selected가 false면 true로 변경
            if (!existTag.selected) {
                tagRepository.register(existTag.copy(selected = true))
                return@forEach // 다음 반복
            }

            // existTag가 있고, 이미 selected == true면 아무 것도 안 함
        }
    }
}

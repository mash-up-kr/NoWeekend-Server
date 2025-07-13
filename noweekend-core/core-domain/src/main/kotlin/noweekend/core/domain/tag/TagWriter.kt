package noweekend.core.domain.tag

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class TagWriter(
    private val tagRepository: TagRepository,
) {
    fun registerSelectedBasicTag(basicTags: List<BasicTag>, userId: String) {
        val existingTags: List<Tag> = tagRepository.findAllByUserId(userId)
        basicTags.forEach { basicTag ->
            val content = basicTag.koreanContent
            val existTag = existingTags.firstOrNull { it.content == content }

            when {
                // a) 없으면 INSERT
                existTag == null -> {
                    tagRepository.register(
                        Tag.register(content = content, userId = userId),
                    )
                }
                // b) 있는데 selected == false 면 SELECTED=true 로 UPDATE
                !existTag.selected -> {
                    tagRepository.register(existTag.copy(selected = true))
                }
                else -> {
                    // nothing
                }
            }
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

package noweekend.core.domain.schedule

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class TagReader(
    private val tagRepository: TagRepository,
) {
    fun getDefaultTags(): List<String> {
        return enumValues<BasicTag>().map { it.koreanContent }
    }

    fun getUserTags(userId: String): UserTags {
        val allUserTags = tagRepository.findAllByUserId(userId)
        val basicTagNames = BasicTag.entries.map { it.koreanContent }

        // 1. 기본 태그(기본으로 정의된 값)
        val selectedBasicTags = allUserTags.filter { it.content in basicTagNames && it.selected }

        // unselected: BasicTag는 정의되어 있는데, 해당 태그가 Tag로 등록돼 있고 selected=false이거나,
        // 아직 한 번도 등록한 적 없는 경우(=selected=false, 새 Tag로 만들어서 내려줘야 함)
        val unselectedBasicTags = mutableListOf<Tag>()
        for (basic in BasicTag.entries) {
            val tag = allUserTags.find { it.content == basic.koreanContent }
            if (tag == null) {
                // 등록된 적이 없는 기본 태그
                unselectedBasicTags.add(
                    Tag(
                        id = "BASIC_TAG",
                        content = basic.koreanContent,
                        userId = userId,
                        selected = false,
                    ),
                )
            } else if (!tag.selected) {
                // 등록됐지만 unselected 상태인 기본 태그
                unselectedBasicTags.add(tag)
            }
        }

        // 2. 커스텀 태그(기본 태그에 없는 것)
        val selectedCustomTags = allUserTags.filter { it.content !in basicTagNames && it.selected }
        val unselectedCustomTags = allUserTags.filter { it.content !in basicTagNames && !it.selected }

        return UserTags(
            selectedBasicTags = selectedBasicTags,
            unselectedBasicTags = unselectedBasicTags,
            selectedCustomTags = selectedCustomTags,
            unselectedCustomTags = unselectedCustomTags,
        )
    }
}

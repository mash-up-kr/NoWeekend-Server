package noweekend.core.domain.tag

import noweekend.core.domain.util.IdGenerator

data class Tag(
    val id: String,
    val content: String,
    val userId: String,
    val selected: Boolean,
) {
    companion object {
        fun register(content: String, userId: String): Tag {
            return Tag(
                id = IdGenerator.generate(),
                content = content,
                userId = userId,
                selected = true,
            )
        }
        fun basicUnselected(basic: BasicTag, userId: String): Tag =
            Tag(
                id = IdGenerator.generate(),
                content = basic.koreanContent,
                userId = userId,
                selected = false,
            )
    }
}

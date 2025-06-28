package noweekend.core.domain.mypage

import noweekend.core.api.controller.v1.request.EditNickname
import noweekend.core.domain.user.UserReader
import noweekend.core.domain.user.UserWriter
import org.springframework.stereotype.Service

@Service
class MyPageServiceImpl(
    private val userReader: UserReader,
    private val userWriter: UserWriter,
) : MyPageService {
    override fun updateNickname(editNickname: EditNickname, userId: String) {
        val user = userReader.findUserById(userId) ?: throw NoSuchElementException()
        val merged = user.copy(
            name = editNickname.nickname,
        )
        userWriter.upsert(merged)
    }
}

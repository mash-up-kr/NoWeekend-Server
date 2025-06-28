package noweekend.core.domain.mypage

import noweekend.core.api.controller.v1.request.EditNickname

interface MyPageService {
    fun updateNickname(editNickname: EditNickname, userId: String)
}

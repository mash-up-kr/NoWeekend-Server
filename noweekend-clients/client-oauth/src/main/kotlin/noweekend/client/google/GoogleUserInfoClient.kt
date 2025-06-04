package noweekend.client.google

import noweekend.client.model.GoogleEmailResult
import org.springframework.stereotype.Component

@Component
class GoogleUserInfoClient internal constructor(
    private val googleUserInfoApi: GoogleUserInfoApi,
) {
    fun getEmail(request: GoogleUserInfoRequest): GoogleEmailResult {
        val tokenHeader = "Bearer ${request.accessToken}"
        val response = googleUserInfoApi.getUserInfo(tokenHeader)
        return response.result()
    }
}

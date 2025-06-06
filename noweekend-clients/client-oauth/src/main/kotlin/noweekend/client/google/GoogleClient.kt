package noweekend.client.google

import noweekend.client.model.GoogleEmailResult
import org.springframework.stereotype.Component

@Component
class GoogleClient internal constructor(
    private val googleApi: GoogleApi,
) {
    fun getEmail(request: GoogleUserInfoRequest): GoogleEmailResult {
        val tokenHeader = "Bearer ${request.accessToken}"
        val response = googleApi.getUserInfo(tokenHeader)
        return response.result()
    }
}

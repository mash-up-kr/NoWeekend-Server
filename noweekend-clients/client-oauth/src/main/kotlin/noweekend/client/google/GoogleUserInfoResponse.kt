package noweekend.client.google

import noweekend.client.model.GoogleEmailResult

data class GoogleUserInfoResponse(
    val email: String?,
) {
    fun result(): GoogleEmailResult {
        return GoogleEmailResult(
            email = email ?: throw IllegalStateException("Email is missing from Google userinfo response."),
        )
    }
}

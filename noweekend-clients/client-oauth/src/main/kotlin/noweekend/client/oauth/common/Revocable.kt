package noweekend.client.oauth.common

interface Revocable {
    fun revokeToken(token: String): Boolean
}

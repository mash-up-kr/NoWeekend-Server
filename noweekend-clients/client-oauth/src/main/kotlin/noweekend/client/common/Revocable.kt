package noweekend.client.common

interface Revocable {
    fun revokeToken(token: String): Boolean
}

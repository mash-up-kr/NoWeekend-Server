package noweekend.client.apple

data class ApplePublicKeyResponse(val keys: List<ApplePublicKey>)

data class ApplePublicKey(
    val kty: String,
    val kid: String,
    val use: String,
    val alg: String,
    val n: String,
    val e: String,
)

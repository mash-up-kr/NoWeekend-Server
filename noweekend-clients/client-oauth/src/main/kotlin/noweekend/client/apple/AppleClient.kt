package noweekend.client.apple

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import noweekend.client.common.OAuthClient
import noweekend.client.common.OAuthInfo
import noweekend.client.common.OAuthLoginParams
import noweekend.client.common.Revocable
import noweekend.client.properties.AppleAuthProperties
import noweekend.core.domain.enumerate.ProviderType
import org.bouncycastle.util.io.pem.PemReader
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Component
import java.io.FileReader
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Base64
import java.util.Date

@Component
class AppleClient internal constructor(
    private val appleApi: AppleApi,
    private val appleOAuthProperties: AppleAuthProperties,
    private val objectMapper: ObjectMapper,
) : OAuthClient, Revocable {
    override fun supports(providerType: ProviderType): Boolean {
        return providerType == ProviderType.APPLE
    }

    override fun requestOAuthInfo(params: OAuthLoginParams): OAuthInfo {
        val appleTokens = requestAccessToken(params)
        if (appleTokens?.idToken == null) throw IllegalStateException("cannot find idToken from apple")
        val claims = getClaims(appleTokens.idToken)
        return AppleOAuthInfo(claims.subject, appleTokens.refreshToken)
    }

    override fun revokeToken(token: String): Boolean {
        val clientId = appleOAuthProperties.appId
        val clientSecret = makeClientSecret(
            keyId = appleOAuthProperties.keyId,
            teamId = appleOAuthProperties.teamId,
            clientId = clientId,
            secretKeyFilePath = appleOAuthProperties.secretKeyFilePath,
        )
        return appleApi.revokeToken(clientId, clientSecret, token).statusCode.is2xxSuccessful
    }

    private fun requestAccessToken(params: OAuthLoginParams): AppleTokens? {
        val clientId = appleOAuthProperties.appId
        val clientSecret = makeClientSecret(
            keyId = appleOAuthProperties.keyId,
            teamId = appleOAuthProperties.teamId,
            clientId = clientId,
            secretKeyFilePath = appleOAuthProperties.secretKeyFilePath,
        )

        return appleApi.getAccessToken(params.getCode(), clientId, clientSecret, AUTHORIZATION_CODE)
    }

    private fun makeClientSecret(keyId: String, teamId: String, clientId: String, secretKeyFilePath: String): String {
        val now = LocalDateTime.now()
        val expirationDate = Date.from(now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant())
        return Jwts.builder()
            .setHeaderParam(KEY_KID, keyId)
            .setHeaderParam(KEY_ALGORITHM, "ES256")
            .setIssuer(teamId)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(expirationDate)
            .setAudience(APPLE_AUDIENCE)
            .setSubject(clientId)
            .signWith(getPrivateKey(secretKeyFilePath), SignatureAlgorithm.ES256)
            .compact()
    }

    private fun getPrivateKey(secretKeyPath: String): PrivateKey {
        val resource = FileSystemResource(secretKeyPath)
        val keyReader = FileReader(resource.uri.path)
        PemReader(keyReader).use { reader ->
            val content = reader.readPemObject().content
            return KeyFactory.getInstance("EC")
                .generatePrivate(PKCS8EncodedKeySpec(content))
        }
    }

    private fun getClaims(identityToken: String): Claims {
        val header = identityToken.substring(0, identityToken.indexOf("."))
        val headerMap: Map<String, String> = objectMapper.readValue(String(Base64.getDecoder().decode(header)))
        val key = getPublicKey(headerMap[KEY_KID], headerMap[KEY_ALGORITHM])

        val nBytes = Base64.getUrlDecoder().decode(key.n)
        val eBytes = Base64.getUrlDecoder().decode(key.e)

        val n = BigInteger(1, nBytes)
        val e = BigInteger(1, eBytes)

        val publicKeySpec = RSAPublicKeySpec(n, e)
        val keyFactory = KeyFactory.getInstance(key.kty)
        val publicKey = keyFactory.generatePublic(publicKeySpec)

        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(identityToken).body
    }

    private fun getPublicKey(kid: String?, alg: String?): ApplePublicKey {
        if (kid == null || alg == null) throw IllegalStateException("cannot get public key from apple")
        return appleApi.getPublicKeys().keys.firstOrNull { key -> key.kid == kid && key.alg == alg }
            ?: throw IllegalStateException("cannot find public key from apple")
    }

    companion object {
        private const val KEY_KID = "kid"
        private const val KEY_ALGORITHM = "alg"

        private const val AUTHORIZATION_CODE = "authorization_code"
        private const val APPLE_AUDIENCE = "https://appleid.apple.com"
    }
}

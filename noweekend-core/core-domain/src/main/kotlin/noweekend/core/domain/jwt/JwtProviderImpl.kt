package noweekend.core.domain.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtProviderImpl(private val jwtProperties: JwtProperties) : JwtProvider {

    private val key = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())

    override fun validate(token: String): Boolean {
        return runCatching {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        }.getOrElse {
            false
        }
    }

    override fun getUserIdFromToken(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        return claims.audience.toString()
    }

    override fun generate(userId: String): String {
        val claims = Jwts
            .claims()
            .setAudience(userId)
            .setIssuer(jwtProperties.issuer)

        val now = Date()
        val validity = Date(now.time + jwtProperties.expiration)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
}

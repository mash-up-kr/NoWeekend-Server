package noweekend.client.apple

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    value = "apple-api",
    url = "\${oauth.apple.api-url}",
)
internal interface AppleApi {
    @GetMapping(
        value = ["/auth/keys"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun getPublicKeys(): ApplePublicKeyResponse

    @PostMapping(
        value = ["/auth/token"],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
    )
    fun getAccessToken(
        @RequestParam("code") code: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("grant_type") grantType: String,
    ): AppleTokens

    @PostMapping(
        value = ["/auth/revoke"],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
    )
    fun revokeToken(
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("token") token: String,
    ): ResponseEntity<Any>
}

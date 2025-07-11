package noweekend.client.oauth.apple

import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

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
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun getAccessToken(
        @RequestBody requset: String,
    ): AppleTokens

    @PostMapping(
        value = ["/auth/revoke"],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun revokeToken(@RequestBody request: String): ResponseEntity<Any>
}

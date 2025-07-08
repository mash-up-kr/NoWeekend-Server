package noweekend.client.oauth.google

import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    value = "google-token-api",
    url = "\${oauth.google.token-base-url}",
)
internal interface GoogleTokenApi {
    @PostMapping(
        value = ["/token"],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun getGoogleToken(@RequestBody request: String): GoogleTokens
}

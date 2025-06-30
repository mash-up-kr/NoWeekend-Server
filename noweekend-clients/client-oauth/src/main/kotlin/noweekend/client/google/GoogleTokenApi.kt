package noweekend.client.google

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

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
    fun getGoogleToken(
        @RequestParam("code") code: String,
        @RequestParam("redirect_uri") redirectUri: String?,
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("grant_type") grantType: String,
    ): GoogleTokens
}

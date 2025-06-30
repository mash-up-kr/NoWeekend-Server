package noweekend.client.google

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    value = "google-resource-api",
    url = "\${oauth.google.resource-base-url}",
)
internal interface GoogleResourceApi {
    @GetMapping(
        value = ["/oauth2/v1/userinfo"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) accessToken: String): GoogleOAuthInfo
}

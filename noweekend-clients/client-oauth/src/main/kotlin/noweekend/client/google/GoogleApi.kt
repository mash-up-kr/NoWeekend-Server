package noweekend.client.google

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    value = "google-userinfo",
    url = "\${google.api.url}",
)
internal interface GoogleApi {
    @GetMapping(
        value = ["/v1/userinfo"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun getUserInfo(
        @RequestHeader("Authorization") authorization: String,
    ): GoogleUserInfoResponse
}

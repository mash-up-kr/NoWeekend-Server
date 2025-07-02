package noweekend.core.api.controller.v1

import noweekend.core.api.controller.v1.request.LocationRequest
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.domain.user.UserService
import noweekend.core.support.response.ApiResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user/location")
class LocationController(
    private val userService: UserService,
) : LocationControllerDocs {

    @PostMapping
    override fun saveLocation(
        @CurrentUserId userId: String,
        @Validated @RequestBody request: LocationRequest,
    ): ApiResponse<String> {
        userService.updateLocation(
            request = request,
            userId = userId,
        )
        return ApiResponse.success("위치 저장됨: ${request.latitude}, ${request.longitude}")
    }
}

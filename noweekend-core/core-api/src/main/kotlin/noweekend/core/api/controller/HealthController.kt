package noweekend.core.api.controller

import io.swagger.v3.oas.annotations.Hidden
import noweekend.core.domain.auth.TestUserService
import noweekend.core.domain.auth.UserWithToken
import noweekend.core.support.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Hidden
@RestController
class HealthController(
    private val testUserService: TestUserService,
) {
    @GetMapping("/health")
    fun health(): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.OK)
            .body("ok")
    }

    @GetMapping("/test-gen")
    fun createTestGen(): ApiResponse<UserWithToken> {
        return ApiResponse.success(
            testUserService.testUserGen(),
        )
    }
}

package noweekend.core.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.core.domain.auth.TestUserService
import noweekend.core.domain.auth.UserWithToken
import noweekend.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@Tag(name = "임시 API", description = "임시 유저 생성 관련 API (추후 삭제 예정)")
@RestController
class TempController(
    private val testUserService: TestUserService,
) {

    @Operation(
        summary = "임시 유저 생성",
        description = """
            임시로 존재하는 API입니다.  
            임시 유저를 생성하며, 인증 토큰(jwtToken) 정보를 함께 반환합니다.
        """,
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "임시 유저 생성 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = UserWithToken::class),
                    ),
                ],
            ),
        ],
    )
    @GetMapping("/test-gen")
    fun createTestGen(): ApiResponse<UserWithToken> {
        return ApiResponse.success(
            testUserService.testUserGen(),
        )
    }
}

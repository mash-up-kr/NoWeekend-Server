package noweekend.core.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.core.domain.auth.TestUserService
import noweekend.core.domain.auth.UserWithToken
import noweekend.core.domain.sandwich.SandwichBatchScheduler
import noweekend.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@Tag(name = "임시 API", description = "임시 유저 생성 관련 API (추후 삭제 예정)")
@RestController
class TempController(
    private val testUserService: TestUserService,
    private val sandwichBatchScheduler: SandwichBatchScheduler,
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

    @PostMapping("/sandwich")
    fun generateSandwichCacheManual(): ApiResponse<String> {
        val now = java.time.LocalDateTime.now()
        val yesterday = now.minusDays(1).toLocalDate()

        sandwichBatchScheduler.generateAndSaveSandwichRecommend(
            yesterday.atTime(22, 0, 0),
        )
//        sandwichBatchScheduler.generateAndSaveSandwichRecommend(
//            yesterday.atTime(23, 0, 0)
//        )

        // === 3~4시 ===
        // sandwichBatchScheduler.generateAndSaveSandwichRecommend(
        //     yesterday.atTime(3, 0, 0)
        // )
        // sandwichBatchScheduler.generateAndSaveSandwichRecommend(
        //     yesterday.atTime(4, 0, 0)
        // )

        // === 5~6시 ===
        // sandwichBatchScheduler.generateAndSaveSandwichRecommend(
        //     yesterday.atTime(5, 0, 0)
        // )
        // sandwichBatchScheduler.generateAndSaveSandwichRecommend(
        //     yesterday.atTime(6, 0, 0)
        // )

        // 필요할 때 위 주석을 풀어서 실행!

        return ApiResponse.success("어제 1시~2시 샌드위치 캐시 생성 완료 (필요시 주석 풀어서 더 추가 가능)")
    }
}

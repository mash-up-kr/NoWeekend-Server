package noweekend.core.api.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.ProfileRequest
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.support.response.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@Tag(name = "마이페이지", description = "정보 수정 API")
interface MyPageControllerDocs {

    @Operation(
        summary = "프로필: 닉네임/생년월일 수정",
        description = "유저가 마이페이지에서 닉네임과 생년월일을 수정합니다.",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ProfileRequest::class),
                    examples = [
                        ExampleObject(
                            name = "예시 요청",
                            value = """
                        {
                          "nickname": "김매송..",
                          "birthDate": "19991213"
                        }
                        """,
                        ),
                    ],
                ),
            ],
        ),
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "프로필 수정 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
                            "프로필이 성공적으로 변경 되었습니다."
                            """,
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
            ),
        ],
    )
    fun updateProfile(
        @Parameter(hidden = true) @CurrentUserId userId: String,
        profileRequest: ProfileRequest,
    ): ApiResponse<String>

    @Operation(
        summary = "프로필: 연차 정보 수정",
        description = "유저가 연차 정보를 수정합니다.",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = LeaveInputRequest::class),
                    examples = [
                        ExampleObject(
                            name = "예시 요청",
                            value = """
                        {
                          "days": 8,
                          "hours": 4
                        }
                        """,
                        ),
                    ],
                ),
            ],
        ),
        responses = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "연차 정보 수정 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
                                "연차 정보가 성공적으로 변경되었습니다."
                            """,
                            ),
                        ],
                    ),
                ],
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
            ),
        ],
    )
    fun updateLeave(
        @Parameter(hidden = true) @CurrentUserId userId: String,
        request: LeaveInputRequest,
    ): ApiResponse<String>
}

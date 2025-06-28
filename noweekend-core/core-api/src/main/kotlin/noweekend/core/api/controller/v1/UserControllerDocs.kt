package noweekend.core.api.controller.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import noweekend.core.api.controller.v1.request.LeaveInputRequest
import noweekend.core.api.controller.v1.request.OnboardingRequest
import noweekend.core.api.controller.v1.request.ScheduleRequest
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.support.response.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@Tag(name = "온보딩", description = "온보딩 등록 API")
interface UserControllerDocs {
    @Operation(
        summary = "온보딩: 자주하는 일정 등록",
        description = "유저가 온보딩 시 자주하는 일정 태그(영문, Enum)를 리스트로 등록합니다.",
        requestBody = RequestBody(
            required = true,
            content = arrayOf(
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ScheduleRequest::class),
                    examples = arrayOf(
                        ExampleObject(
                            name = "예시 요청",
                            value = """
                        {
                          "scheduleTags": ["COMMUTE", "LEAVE", "GYM"]
                        }
                        """,
                        ),
                    ),
                ),
            ),
        ),
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "일정 등록 성공",
                content = arrayOf(
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class),
                        examples = arrayOf(
                            ExampleObject(
                                name = "예시 응답",
                                value = """
                                "일정 등록이 성공적으로 완료되었습니다."
                                """,
                            ),
                        ),
                    ),
                ),
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
            ),
        ],
    )
    fun saveUserSchedules(
        @Parameter(hidden = true) @CurrentUserId userId: String,
        request: ScheduleRequest,
    ): ApiResponse<String>

    @Operation(
        summary = "온보딩: 닉네임/생년월일 등록",
        description = "유저가 온보딩 시 닉네임과 생년월일을 등록합니다.",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = OnboardingRequest::class),
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
                description = "닉네임 및 생년월일 등록 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
                            "닉네임 및 생년월일 등록이 성공적으로 완료되었습니다."
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
    fun submitProfile(request: OnboardingRequest): ApiResponse<String>

    @Operation(
        summary = "온보딩: 올해 연차 입력",
        description = "유저가 올해 남은 연차(일, 시간 단위)를 입력합니다.",
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
                          "days": 5,
                          "hours": 4
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
                description = "연차 등록 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
                            "연차 정보가 성공적으로 저장되었습니다."
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
    fun submitLeave(request: LeaveInputRequest): ApiResponse<String>
}

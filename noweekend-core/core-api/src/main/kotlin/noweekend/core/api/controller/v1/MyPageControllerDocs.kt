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
import noweekend.core.api.controller.v1.request.TagUpdateRequest
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.domain.tag.UserTags
import noweekend.core.support.response.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@Tag(name = "마이페이지", description = "정보 수정 API")
interface MyPageControllerDocs {

    @Operation(
        summary = "마이페이지: 닉네임/생년월일 수정",
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
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "SUCCESS",
  "data": "프로필이 성공적으로 변경 되었습니다.",
  "error": null
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "올바르지 않은 요청입니다.",
    "data": {}
  }
}
""",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun updateProfile(
        @Parameter(hidden = true) @CurrentUserId userId: String,
        profileRequest: ProfileRequest,
    ): ApiResponse<String>

    @Operation(
        summary = "마이페이지: 연차 정보 수정",
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
            SwaggerApiResponse(
                responseCode = "200",
                description = "연차 정보 수정 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "SUCCESS",
  "data": "연차 정보가 성공적으로 변경되었습니다.",
  "error": null
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "올바르지 않은 요청입니다.",
    "data": {}
  }
}
""",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun updateLeave(
        @Parameter(hidden = true) @CurrentUserId userId: String,
        request: LeaveInputRequest,
    ): ApiResponse<String>

    @Operation(
        summary = "마이페이지: 태그(자주하는 일정) 조회",
        description = "유저의 선택/미선택 기본 태그와 커스텀 태그 목록을 반환합니다.",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "태그 조회 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "SUCCESS",
  "data": {
    "selectedBasicTags": [
        {
            "id": "baf123e1-aaaa-bbbb-cccc-1234567890ab",
            "content": "회의 참석",
            "userId": "user-1",
            "selected": true
        },
        {
            "id": "baf123e1-aaaa-bbbb-cccc-2234567890ab",
            "content": "점심 식사 약속",
            "userId": "user-1",
            "selected": true
        }
    ],
    "unselectedBasicTags": [
        {
            "id": "BASIC_TAG",
            "content": "헬스장 운동",
            "userId": "user-1",
            "selected": false
        },
        {
            "id": "baf123e1-aaaa-bbbb-cccc-3234567890ab",
            "content": "장 보기 / 마트 가기",
            "userId": "user-1",
            "selected": false
        }
    ],
    "selectedCustomTags": [
        {
            "id": "baf123e1-aaaa-bbbb-cccc-4234567890ab",
            "content": "스터디 그룹",
            "userId": "user-1",
            "selected": true
        }
    ],
    "unselectedCustomTags": [
        {
            "id": "baf123e1-aaaa-bbbb-cccc-5234567890ab",
            "content": "비즈니스 미팅",
            "userId": "user-1",
            "selected": false
        }
    ]
  },
  "error": null
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "올바르지 않은 요청입니다.",
    "data": {}
  }
}
""",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun getTags(
        @Parameter(hidden = true) @CurrentUserId userId: String,
    ): ApiResponse<UserTags>

    @Operation(
        summary = "마이페이지: 태그(자주하는 일정) 수정",
        description = "유저가 자신의 기본/커스텀 태그(자주하는 일정)를 추가·삭제(업서트)합니다.",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = TagUpdateRequest::class),
                    examples = [
                        ExampleObject(
                            name = "예시 요청",
                            value = """
{
  "addScheduleTags": ["회의 참석", "헬스장 운동", "스터디 그룹"],
  "deleteScheduleTags": ["장 보기 / 마트 가기", "비즈니스 미팅"]
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
                description = "태그(자주하는 일정) 수정 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "SUCCESS",
  "data": "자주하는 일정 태그가 성공적으로 수정되었습니다.",
  "error": null
}
""",
                            ),
                        ],
                    ),
                ],
            ),
            SwaggerApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "INVALID_PARAMETER",
    "message": "올바르지 않은 요청입니다.",
    "data": {}
  }
}
""",
                            ),
                        ],
                    ),
                ],
            ),
        ],
    )
    fun updateTags(
        @Parameter(hidden = true) @CurrentUserId userId: String,
        request: TagUpdateRequest,
    ): ApiResponse<String>
}

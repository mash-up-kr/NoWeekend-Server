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
import noweekend.core.api.controller.v1.request.TagRequest
import noweekend.core.api.controller.v1.response.DefaultTags
import noweekend.core.api.security.annotations.CurrentUserId
import noweekend.core.support.response.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@Tag(name = "온보딩", description = "온보딩 등록 API")
interface OnboardingControllerDocs {

    @Operation(
        summary = "온보딩: 자주하는 일정 등록",
        description = "유저가 온보딩 시 자주하는 일정 태그(영문, Enum)를 리스트로 등록합니다.",
        requestBody = RequestBody(
            required = true,
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = TagRequest::class),
                    examples = [
                        ExampleObject(
                            name = "예시 요청",
                            value = """
{
  "scheduleTags": ["집안일", "은행 업무", "관공서 업무"]
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
                description = "일정 등록 성공",
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
  "data": "일정 등록이 성공적으로 완료되었습니다.",
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
    fun registerSelectedDefaultTag(
        @Parameter(hidden = true) @CurrentUserId userId: String,
        request: TagRequest,
    ): ApiResponse<String>

    @Operation(
        summary = "온보딩: 닉네임/생년월일 등록",
        description = "유저가 온보딩 시 닉네임과 생년월일을 등록합니다.",
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
                description = "닉네임 및 생년월일 등록 성공",
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
  "data": "닉네임 및 생년월일 등록이 성공적으로 완료되었습니다.",
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
    fun submitProfile(
        @Parameter(hidden = true) @CurrentUserId userId: String,
        request: ProfileRequest,
    ): ApiResponse<String>

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
                        schema = Schema(implementation = ApiResponse::class),
                        examples = [
                            ExampleObject(
                                name = "예시 응답",
                                value = """
{
  "result": "SUCCESS",
  "data": "연차 정보가 성공적으로 저장되었습니다.",
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
    fun submitLeave(
        @Parameter(hidden = true) @CurrentUserId userId: String,
        request: LeaveInputRequest,
    ): ApiResponse<String>

    @Operation(
        summary = "온보딩: 기본 일정 태그 조회",
        description = "온보딩 시 선택 가능한 기본 일정 태그(한글)를 모두 반환합니다.",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "기본 일정 태그 조회 성공",
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
    "tags": [
      "회의 참석",
      "점심 식사 약속",
      "헬스장 운동",
      "장 보기 / 마트 가기",
      "가족 모임",
      "병원 예약",
      "카페에서 작업 / 휴식",
      "친구 만남",
      "술자리",
      "스터디",
      "학원 수업",
      "야근",
      "추가 업무",
      "산책",
      "반려동물 산책",
      "집안일",
      "은행 업무",
      "관공서 업무",
      "독서",
      "데이트",
      "미용실",
      "드라이브",
      "나들이",
      "넷플릭스 시청",
      "유튜브 시청",
      "치지직 시청"
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
    fun getDefaultTag(
        @Parameter(hidden = true) @CurrentUserId userId: String,
    ): ApiResponse<DefaultTags>
}

package noweekend.core.support.error

import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ErrorType(val status: HttpStatus, val code: ErrorCode, val message: String, val logLevel: LogLevel) {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ErrorCode.E401, "Unauthorized user", LogLevel.DEBUG),
    FORBIDDEN_ERROR(HttpStatus.FORBIDDEN, ErrorCode.E403, "The request is forbidden.", LogLevel.ERROR),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, ErrorCode.E404, "The requested resource was not found.", LogLevel.ERROR),
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.", LogLevel.ERROR),
    INVALID_PROVIDER_TYPE(HttpStatus.BAD_REQUEST, ErrorCode.E400, "지원하지 않는 providerType입니다.", LogLevel.INFO),
    USER_NOT_FOUND_INTERNAL(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "사용자를 찾을 수 없습니다. - 서버 오류", LogLevel.ERROR),
    USER_LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.E404, "사용자의 위치 정보가 존재하지 않습니다. 위치를 생성 후 요청해주세요.", LogLevel.INFO),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, ErrorCode.E400, "잘못된 요청입니다.", LogLevel.WARN),
    USER_TAGS_ERROR(HttpStatus.BAD_REQUEST, ErrorCode.E400, "사용자의 태그가 3개 미만입니다. 초기화되지 않았습니다.", LogLevel.ERROR),
    SERVER_TAGS_ERROR(HttpStatus.NOT_FOUND, ErrorCode.E404, "MCP 추천 서버에서 장애가 발생했습니다. 새로운 태그를 추천할 수 없습니다.", LogLevel.ERROR),
}

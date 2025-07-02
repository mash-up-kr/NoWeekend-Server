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
}

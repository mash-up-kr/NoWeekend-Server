package noweekend.core.support.error

import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ErrorType(val status: HttpStatus, val code: ErrorCode, val message: String, val logLevel: LogLevel) {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ErrorCode.E401, "Unauthorized user", LogLevel.DEBUG),
    FORBIDDEN_ERROR(HttpStatus.FORBIDDEN, ErrorCode.E403, "The request is forbidden.", LogLevel.ERROR),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, ErrorCode.E404, "The requested resource was not found.", LogLevel.ERROR),
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.", LogLevel.ERROR),
}

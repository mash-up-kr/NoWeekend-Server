package noweekend.core.api.controller

import jakarta.validation.ConstraintViolationException
import noweekend.core.support.error.CoreException
import noweekend.core.support.error.ErrorType
import noweekend.core.support.response.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.logging.LogLevel
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ResponseEntity<ApiResponse<Any>> {
        when (e.errorType.logLevel) {
            LogLevel.ERROR -> log.error("CoreException : {}", e.message, e)
            LogLevel.WARN -> log.warn("CoreException : {}", e.message, e)
            else -> log.info("CoreException : {}", e.message, e)
        }
        return ResponseEntity(ApiResponse.error(e.errorType, e.message, e.data), e.errorType.status)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(ErrorType.DEFAULT_ERROR), ErrorType.DEFAULT_ERROR.status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Any>> {
        val errors = e.bindingResult.fieldErrors.map { fieldError ->
            mapOf(
                "field" to fieldError.field,
                "rejectedValue" to fieldError.rejectedValue,
                "message" to fieldError.defaultMessage,
            )
        }

        log.warn("Validation failed: {}", errors)

        return ResponseEntity(
            ApiResponse.error(
                error = ErrorType.INVALID_PARAMETER,
                errorMessage = "Invalid request parameters",
                errorData = errors,
            ),
            ErrorType.INVALID_PARAMETER.status,
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException): ResponseEntity<ApiResponse<Any>> {
        val errors = e.constraintViolations.map { cv ->
            mapOf(
                "path" to cv.propertyPath.toString(),
                "invalid" to cv.invalidValue,
                "message" to cv.message,
            )
        }

        log.warn("Constraint violations: {}", errors)

        return ResponseEntity(
            ApiResponse.error(
                error = ErrorType.INVALID_PARAMETER,
                errorMessage = "Invalid request parameters",
                errorData = errors,
            ),
            ErrorType.INVALID_PARAMETER.status,
        )
    }

    /**
     *  GET/POST/... 요청 시 @RequestParam, @RequestHeader, @ModelAttribute 등
     *  필수 파라미터가 없으면 이 예외가 발생합니다.
     */
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingRequestParam(e: MissingServletRequestParameterException): ResponseEntity<ApiResponse<Any>> {
        val errorDetail = mapOf(
            "parameter" to e.parameterName,
            "message" to "${e.parameterName} parameter is required",
        )
        log.warn("Missing request parameter: {}", errorDetail)
        return ResponseEntity(
            ApiResponse.error(
                error = ErrorType.INVALID_PARAMETER,
                errorMessage = "Required request parameter is missing",
                errorData = errorDetail,
            ),
            ErrorType.INVALID_PARAMETER.status,
        )
    }

    /**
     *  @PathVariable 로 바인딩할 경로 변수가 없으면 이 예외가 발생합니다.
     */
    @ExceptionHandler(MissingPathVariableException::class)
    fun handleMissingPathVariable(e: MissingPathVariableException): ResponseEntity<ApiResponse<Any>> {
        val errorDetail = mapOf(
            "variable" to e.variableName,
            "message" to "${e.variableName} path variable is required",
        )
        log.warn("Missing path variable: {}", errorDetail)
        return ResponseEntity(
            ApiResponse.error(
                error = ErrorType.INVALID_PARAMETER,
                errorMessage = "Required path variable is missing",
                errorData = errorDetail,
            ),
            ErrorType.INVALID_PARAMETER.status,
        )
    }

    /**
     *  @RequestBody DTO가 비어 있거나, JSON 파싱 자체가 실패할 때 발생합니다.
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(e: HttpMessageNotReadableException): ResponseEntity<ApiResponse<Any>> {
        log.warn("Malformed JSON request: {}", e.message)
        return ResponseEntity(
            ApiResponse.error(
                error = ErrorType.INVALID_PARAMETER,
                errorMessage = "Malformed or missing request body",
                errorData = mapOf("error" to e.localizedMessage),
            ),
            ErrorType.INVALID_PARAMETER.status,
        )
    }
}

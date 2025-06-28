package noweekend.core.support.error

class InvalidTokenException(
    errorMessage: String? = null,
) : CoreException(errorType = ErrorType.UNAUTHORIZED, errorMessage = errorMessage)

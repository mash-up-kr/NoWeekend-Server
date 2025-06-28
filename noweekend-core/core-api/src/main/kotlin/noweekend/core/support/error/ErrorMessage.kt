package noweekend.core.support.error

data class ErrorMessage private constructor(
    val code: String,
    val message: String,
    val data: Any? = null,
) {
    constructor(errorType: ErrorType, data: Any? = null) : this(
        code = errorType.code.name,
        message = errorType.message,
        data = data,
    )

    constructor(errorType: ErrorType, errorMessage: String? = null, data: Any? = null) : this(
        code = errorType.code.name,
        message = errorMessage ?: errorType.message,
        data = data,
    )
}

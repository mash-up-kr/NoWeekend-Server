package noweekend.core.support.error

open class CoreException(
    val errorType: ErrorType,
    val data: Any? = null,
    errorMessage: String? = null,
) : RuntimeException(errorMessage ?: errorType.message)

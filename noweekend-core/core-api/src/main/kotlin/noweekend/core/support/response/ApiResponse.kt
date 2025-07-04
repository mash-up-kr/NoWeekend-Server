package noweekend.core.support.response

import noweekend.core.support.error.ErrorMessage
import noweekend.core.support.error.ErrorType

data class ApiResponse<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val error: ErrorMessage? = null,
) {
    companion object {
        fun success(): ApiResponse<Any> {
            return ApiResponse(ResultType.SUCCESS, null, null)
        }

        fun <S> success(data: S): ApiResponse<S> {
            return ApiResponse(ResultType.SUCCESS, data, null)
        }

        fun <S> error(error: ErrorType, errorMessage: String? = null, errorData: Any? = null): ApiResponse<S> {
            return ApiResponse(
                result = ResultType.ERROR,
                data = null,
                error = ErrorMessage(error, errorMessage ?: error.message, errorData),
            )
        }
    }
}

package noweekend.core.api.controller.v1.request

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [BirthDateValidator::class])
annotation class ValidBirthDate(
    val message: String = "올바른 생년월일(yyyyMMdd)이 아닙니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class BirthDateValidator : ConstraintValidator<ValidBirthDate, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value.isNullOrBlank()) return false
        if (!Regex("^\\d{8}\$").matches(value)) return false
        return try {
            val date = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd"))
            date.isAfter(LocalDate.of(1900, 1, 1)) && !date.isAfter(LocalDate.now())
        } catch (e: DateTimeParseException) {
            false
        }
    }
}

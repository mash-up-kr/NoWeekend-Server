package noweekend.core.api.controller.v1.request

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [HoursValidator::class])
annotation class ValidLeaveHours(
    val message: String = "연차 시간은 0 또는 4만 가능합니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class HoursValidator : ConstraintValidator<ValidLeaveHours, Int> {
    override fun isValid(value: Int?, context: ConstraintValidatorContext?): Boolean {
        return value == 0 || value == 4
    }
}

package eu.kotlinBoilerplate.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.math.BigDecimal
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [TwoDecimalPlacesValidator::class])
annotation class TwoDecimalPlaces(
    val message: String = "Must have at most 2 decimal places",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

open class TwoDecimalPlacesValidator : ConstraintValidator<TwoDecimalPlaces, BigDecimal> {
    override fun isValid(value: BigDecimal?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return true // Allow null for optional fields
        return value.scale() <= 2
    }
}

package eu.kotlinBoilerplate.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [OnlyEnumValueValidator::class])
annotation class OnlyEnumValue(
    val enumClass: KClass<out Enum<*>>, // Enum class
    val message: String = "Value must be one of the allowed enum values",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class OnlyEnumValueValidator : ConstraintValidator<OnlyEnumValue, Enum<*>?> {

    private lateinit var allowedValues: Set<String>

    override fun initialize(annotation: OnlyEnumValue) {
        allowedValues = annotation.enumClass.java.enumConstants.map { it.name }.toSet()
    }

    override fun isValid(value: Enum<*>?, context: ConstraintValidatorContext): Boolean {
        return value == null || allowedValues.contains(value.name)
    }
}
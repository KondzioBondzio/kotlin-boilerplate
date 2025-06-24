import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.UUID
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UUIDValidator::class])
annotation class ValidUUID(
    val message: String = "Invalid UUID format",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class UUIDValidator : ConstraintValidator<ValidUUID, Any?> {
    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        return when (value) {
            null -> true
            is UUID -> true
            is String -> runCatching { UUID.fromString(value) }.isSuccess
            else -> false
        }
    }
}

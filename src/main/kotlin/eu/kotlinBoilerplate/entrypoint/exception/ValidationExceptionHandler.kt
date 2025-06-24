package eu.kotlinBoilerplate.entrypoint.exception

import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.http.ResponseEntity
import eu.kotlinBoilerplate.logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.ConstraintViolationException
import org.springframework.http.converter.HttpMessageNotReadableException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import eu.kotlinBoilerplate.domain.exceptions.DomainException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger by logger()

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(e: DomainException): ResponseEntity<ValidationErrorResponse> {
        val response = ValidationErrorResponse(
            message = e.message ?: "An error occurred",
            errors = mapOf()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }
}


@RestControllerAdvice
class ValidationExceptionHandler {

    private val logger by logger()

    private fun toSnakeCase(input: String): String {
        return input.replace(Regex("([a-z])([A-Z]+)"), "$1_$2").lowercase()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        logger.error("Validation error: ${ex.bindingResult.allErrors}")

        val errors = ex.bindingResult.fieldErrors.groupBy(
            { toSnakeCase(it.field) },
            { it.defaultMessage ?: "Invalid value" }
        )

        val response = ValidationErrorResponse(
            message = "The given data was invalid.",
            errors = errors
        )

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ValidationErrorResponse> {
        logger.error("Constraint violation error: ${ex.constraintViolations}")

        val errors = ex.constraintViolations.groupBy(
            { toSnakeCase(it.propertyPath.toString()) },
            { it.message ?: "Invalid value" }
        )

        val response = ValidationErrorResponse(
            message = "The given data was invalid.",
            errors = errors
        )

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response)
    }


    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<Map<String, Any>> {
        logger.error("Invalid request payload", ex)

        val cause = ex.cause
        val errors = if (cause is InvalidFormatException) {
            cause.path.map { pathReference ->
                pathReference.fieldName to "Invalid format for field '${pathReference.fieldName}'"
            }.toMap()
        } else {
            mapOf("error" to "Invalid request payload")
        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
            mapOf(
                "message" to "The given data was invalid.",
                "errors" to errors
            )
        )
    }
}


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Schema(description = "Validation error response schema")
data class ValidationErrorResponse(
    @Schema(description = "Error message", example = "The given data was invalid.")
    val message: String,
    @Schema(
        description = "Validation errors mapped by field",
        example = """{"field_name": ["Error message 1", "Error message 2"]}"""
    )
    val errors: Map<String, List<String>>
)

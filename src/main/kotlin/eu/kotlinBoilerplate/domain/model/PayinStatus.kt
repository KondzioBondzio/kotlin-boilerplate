package eu.kotlinBoilerplate.domain.model

enum class PayinStatus(val value: String) {
    CREATED("created"),
    PENDING("pending"),
    FAILED("failed"),
    COMPLETED("completed"),
    CANCELLED("cancelled")
}
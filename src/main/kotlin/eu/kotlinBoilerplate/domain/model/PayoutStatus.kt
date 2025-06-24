package eu.kotlinBoilerplate.domain.model

enum class PayoutStatus(val value: String) {
    CREATED("created"),
    PENDING("pending"),
    FAILED("failed"),
    COMPLETED("completed"),
    CANCELLED("cancelled")
}
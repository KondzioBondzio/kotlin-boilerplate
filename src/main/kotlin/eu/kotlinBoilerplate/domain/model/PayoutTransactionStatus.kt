package eu.kotlinBoilerplate.domain.model

enum class PayoutTransactionStatus(val value: String) {
    CREATED("created"),
    PENDING("pending"),
    FAILED("failed"),
    COMPLETED("completed")
}

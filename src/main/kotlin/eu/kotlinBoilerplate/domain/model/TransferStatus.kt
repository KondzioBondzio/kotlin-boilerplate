package eu.kotlinBoilerplate.domain.model

enum class TransferStatus(val value: String) {
    CREATED("created"),
    WAITING_FOR_PAYIN("waiting_for_payin"),
    PENDING("pending"),
    COMPLETED("completed"),
    CANCELLED("cancelled"),
    FAILED("failed")
}
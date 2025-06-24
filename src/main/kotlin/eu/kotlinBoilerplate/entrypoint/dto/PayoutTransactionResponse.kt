package eu.kotlinBoilerplate.entrypoint.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import eu.kotlinBoilerplate.domain.model.PayoutTransaction
import eu.kotlinBoilerplate.domain.model.PayoutTransactionStatus
import eu.kotlinBoilerplate.domain.model.Transfer
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PayoutTransactionResponse(
    val id: UUID,
    val organizationCode: String,
    val status: PayoutTransactionStatus,
    val transfer: Transfer,
    val createdAt: String,
    val updatedAt: String? = null
) {
    companion object {
        fun fromModel(payoutTransaction: PayoutTransaction): PayoutTransactionResponse {
            return PayoutTransactionResponse(
                id = payoutTransaction.id,
                organizationCode = payoutTransaction.organizationCode,
                status = payoutTransaction.status,
                transfer = payoutTransaction.transfer,
                createdAt = payoutTransaction.createdAt.toString(),
                updatedAt = payoutTransaction.updatedAt.toString()
            )
        }
    }
}

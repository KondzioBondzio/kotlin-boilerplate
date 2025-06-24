package eu.kotlinBoilerplate.domain.pain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentInitiation(
    val messageId: String? = null,
    val creationDateTime: LocalDateTime = LocalDateTime.now(),
    val initiatingParty: Party,
    val paymentInfos: List<PaymentInfo>
) {
    fun getTotalTransactions(): Int = paymentInfos.sumOf { it.creditTransfers.size }
    fun getTotalAmount(): BigDecimal = paymentInfos.sumOf { it.getTotalAmount() }
}
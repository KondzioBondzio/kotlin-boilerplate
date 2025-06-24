package eu.kotlinBoilerplate.domain.pain.model

import java.math.BigDecimal
import java.time.LocalDate

data class PaymentInfo(
    val paymentInfoId: String,
    val paymentMethod: String = "TRF",
    val requestedExecutionDate: LocalDate,
    val debtor: Party,
    val debtorAccount: Account,
    val debtorAgent: FinancialInstitution,
    val creditTransfers: List<CreditTransfer>
) {
    fun getTotalAmount(): BigDecimal = creditTransfers.sumOf { it.amount.value }
}
package eu.kotlinBoilerplate.domain.pain.model

data class CreditTransfer(
    val instructionId: String,
    val endToEndId: String,
    val amount: Amount,
    val creditor: Party,
    val creditorAccount: Account,
    val creditorAgent: FinancialInstitution,
    val remittanceInfo: String? = null,
    val purpose: String? = null,
    val chargeBearer: String? = null,
    val instructionPriority: String? = null,
    val serviceLevel: String? = null,
    val categoryPurpose: String? = null
) {
    fun isDomestic(): Boolean = purpose == "PLKR" || creditorAgent.isDomestic()
}
package eu.kotlinBoilerplate.domain.pain.model

data class FinancialInstitution(
    val bic: String? = null,
    val clearingSystemMemberId: String? = null,
    val name: String? = null
) {
    fun isDomestic(): Boolean = clearingSystemMemberId != null
}
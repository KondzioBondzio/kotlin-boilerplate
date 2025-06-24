package eu.kotlinBoilerplate.entrypoint.dto

import java.math.BigDecimal

data class TransferRequest(
    val creditorName: String,
    val creditorIban: String,
    val creditorBic: String? = null,
    val creditorClearingId: String? = null,
    val amount: BigDecimal,
    val currency: String = "PLN",
    val remittanceInfo: String?,
    val purpose: String? = null,
    val chargeBearer: String? = null,
    val instructionPriority: String? = null,
    val serviceLevel: String? = null,
    val categoryPurpose: String? = null,
    val creditorAddressLines: List<String>? = null,
    val creditorCountry: String = "PL"
)
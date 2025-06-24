package eu.kotlinBoilerplate.entrypoint.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PaymentExportRequest(
    val initiatingPartyName: String,
    val initiatingPartyId: String?,
    val initiatingPartyAddressLines: List<String>? = null,
    val initiatingPartyCountry: String = "PL",
    val debtorIban: String,
    val debtorBic: String? = null,
    val debtorClearingId: String? = null,
    val requestedExecutionDate: LocalDate?,
    val transfers: List<TransferRequest>
)
package eu.kotlinBoilerplate.domain.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = WalletPayoutDataDTO::class, name = "wallet"),
    JsonSubTypes.Type(value = BankTransferPayoutDataDTO::class, name = "bank_transfer")
)
sealed class PayoutData {

}

data class BankTransferPayoutDataDTO(
    val bankAccount: String,
    val bankBic: String? = null
) : PayoutData()

data class WalletPayoutDataDTO(
    val walletId: UUID
) : PayoutData()
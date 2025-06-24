package eu.kotlinBoilerplate.domain.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.UUID

@JsonNaming(SnakeCaseStrategy::class)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = WalletPayinDataDTO::class, name = "wallet"),
    JsonSubTypes.Type(value = BankTransferPayinDataDTO::class, name = "bank_transfer"),
    JsonSubTypes.Type(value = CardPayinDataDTO::class, name = "card"),
    JsonSubTypes.Type(value = BlikPayinDataDTO::class, name = "blik"),
    JsonSubTypes.Type(value = PayByLinkPayinDataDTO::class, name = "pay_by_link")
)
sealed class PayinData(
)
{
}

data class WalletPayinDataDTO(
    val id: UUID? = null,
    val walletId: UUID,
    var holdId: UUID? = null
) : PayinData()

data class BankTransferPayinDataDTO(
    val id: UUID? = null,
    val bankName: String? = null,
    val bankAccountNumber: String? = null,
    val swiftCode: String? = null,
    val transferTitle: String? = null,
) : PayinData()

data class CardPayinDataDTO(
    val id: UUID? = null,
    val redirectUrl: String? = null
) : PayinData()

data class BlikPayinDataDTO(
    val id: UUID? = null,
    val redirectUrl: String? = null
) : PayinData()

data class PayByLinkPayinDataDTO(
    val id: UUID? = null,
    val redirectUrl: String? = null
) : PayinData()

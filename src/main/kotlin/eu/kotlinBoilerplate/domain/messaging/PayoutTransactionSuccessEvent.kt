package eu.kotlinBoilerplate.domain.messaging

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PayoutTransactionSuccessEvent (
    override val routingKey: String = "payouts.event.PayoutTransactionSuccess",
    override val organizationCode: String,
    val metadata: PayoutTransactionData
): BaseMessage {
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class PayoutTransactionData(
    val id : UUID,
    val transferId: UUID
)
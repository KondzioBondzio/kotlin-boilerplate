package eu.kotlinBoilerplate.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Document("payouts")
data class Payout(
    var status: PayoutStatus ,
    val method: PayoutType,
    val payoutData: PayoutData,
    val amount: BigDecimal,
    val currency: CurrencyType,
    )

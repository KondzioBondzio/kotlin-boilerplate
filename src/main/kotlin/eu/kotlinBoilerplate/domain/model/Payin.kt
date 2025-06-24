package eu.kotlinBoilerplate.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.math.BigDecimal

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class Payin (
    var status: PayinStatus = PayinStatus.CREATED,
    val method: PayinType,
    var payinData: PayinData,
    val amount: BigDecimal,
    val currency: CurrencyType,
    val commission: BigDecimal ?= BigDecimal.ZERO,
    val totalAmount: BigDecimal ?= BigDecimal.ZERO,
)
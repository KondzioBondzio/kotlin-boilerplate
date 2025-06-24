package eu.kotlinBoilerplate.domain.model

import java.math.BigDecimal

class Commission(
    val amount: BigDecimal,
    val currency: CurrencyType
)
package eu.kotlinBoilerplate.domain.pain.model

import java.math.BigDecimal

data class Amount(
    val value: BigDecimal,
    val currency: String = "PLN"
)
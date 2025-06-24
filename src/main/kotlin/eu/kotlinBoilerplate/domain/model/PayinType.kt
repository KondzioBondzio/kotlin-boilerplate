package eu.kotlinBoilerplate.domain.model

import kotlin.collections.first

enum class PayinType(val value: String) {
    WALLET("wallet"),
    BANK_TRANSFER("bank_transfer"),
    BLIK("blik"),
    CARD("card"),
    PAY_BY_LINK("pay_by_link");

    companion object {
        fun fromValue(value: String): PayinType {
            return entries.first { it.value == value }
        }
    }
}
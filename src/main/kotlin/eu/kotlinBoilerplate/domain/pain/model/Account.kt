package eu.kotlinBoilerplate.domain.pain.model

data class Account(
    val iban: String,
    val currency: String? = null
)
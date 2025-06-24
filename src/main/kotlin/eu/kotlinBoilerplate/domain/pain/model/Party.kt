package eu.kotlinBoilerplate.domain.pain.model

data class Party(
    val name: String,
    val identification: String? = null,
    val address: Address? = null
)
package eu.kotlinBoilerplate.domain.pain.model

data class Address(
    val country: String = "PL",
    val addressLines: List<String>
) {
    constructor(
        streetName: String,
        buildingNumber: String,
        postCode: String,
        townName: String,
        country: String = "PL"
    ) : this(
        country = country,
        addressLines = listOf(
            "$streetName $buildingNumber",
            "$postCode $townName"
        )
    )
}
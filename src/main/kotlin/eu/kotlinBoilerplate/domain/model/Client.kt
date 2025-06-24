package eu.kotlinBoilerplate.domain.model


import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.UUID
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime

enum class ClientStatus(val value: String) {
    ACTIVE("active"),
    BLOCKED("blocked");

    @JsonValue
    fun toValue(): String = value

    companion object {
        @JvmStatic
        @JsonCreator
        fun fromValue(value: String): ClientStatus {
            return ClientStatus.entries.find { it.value.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown ClientStatus: $value")
        }
    }
}


enum class ClientType (val value: String) {
    PERSONAL("personal"),
    COMPANY("company");

    @JsonValue
    fun toValue(): String = value

    companion object {
        @JvmStatic
        @JsonCreator
        fun fromValue(value: String): ClientType {
            return entries.find { it.value.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown ClientType: $value")
        }
    }
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Document("addresses")
data class Address(
    val street: String? = null,
    val houseNr: String? = null,
    val apartmentNr: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val country: String? = null,
    val clientId: UUID? = null
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Document("clients")
data class Client(
    val id: UUID,
    val organizationCode: String,
    val address: Address? = null,
    val email: String? = null,
    val phone: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDate: LocalDate? = null,
    val citizenship: String? = null,
    val pesel: String? = null,
    val nip: String? = null,
    val companyName: String? = null,
    val createdAt: LocalDateTime,
    val status: ClientStatus,
    val type: ClientType
) {
    fun isActive(): Boolean = status == ClientStatus.ACTIVE
}

package eu.kotlinBoilerplate.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.Instant
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Transfer(
    @Id
    @Field(targetType = FieldType.STRING)
    val id: UUID,
    val calculationId: UUID,
    val organizationCode: String,
    val externalId: String?,

    val sender: Client,
    val recipient: Client,

    var status: TransferStatus,

    val fromCountry: String,
    val toCountry: String,

    val revaluation: Revaluation,
    val commission: Commission,

    val payin: Payin,
    val payout: Payout,

    val successUrl: String?,
    val failureUrl: String?,

    val createdAt: Instant ,
    var updatedAt: Instant ,
)
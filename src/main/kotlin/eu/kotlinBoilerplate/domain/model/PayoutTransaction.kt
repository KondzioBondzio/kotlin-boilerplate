package eu.kotlinBoilerplate.domain.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.Instant
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Document("payout_transactions")
data class PayoutTransaction(
    @Id
    @Field(targetType = FieldType.STRING)
    val id: UUID,
    val organizationCode: String,
    var status: PayoutTransactionStatus = PayoutTransactionStatus.CREATED,
    val transfer: Transfer,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    var updatedAt: Instant = Instant.now(),
) {
}
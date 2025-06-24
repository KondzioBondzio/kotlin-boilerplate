package eu.kotlinBoilerplate.domain

import eu.kotlinBoilerplate.domain.model.PayoutTransaction
import eu.kotlinBoilerplate.domain.model.PayoutTransactionStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.Optional
import java.util.UUID

interface PayoutTransactionRepository: MongoRepository<PayoutTransaction, UUID> {
    fun findByIdAndOrganizationCode(
        id: UUID,
        organizationCode: String
    ): Optional<PayoutTransaction>
}

@Repository
class PayoutTransactionCustomRepository(
    private val mongoTemplate: MongoTemplate,
) {
    fun findAllByFilters(
        payoutTransactionId: UUID?,
        transferId: UUID?,
        fromDate: Instant?,
        toDate: Instant?,
        organizationCode: String?,
        status: PayoutTransactionStatus?,
        pageable: Pageable
    ): Page<PayoutTransaction> {
        val criteria = mutableListOf<Criteria>()
        payoutTransactionId?.let { criteria.add(Criteria.where("id").`is`(payoutTransactionId)) }
        transferId?.let { criteria.add(Criteria.where("transfer.id").`is`(transferId)) }
        fromDate?.let { criteria.add(Criteria.where("createdAt").gte(it)) }
        toDate?.let { criteria.add(Criteria.where("createdAt").lte(it)) }
        organizationCode?.let { criteria.add(Criteria.where("organizationCode").`is`(it)) }
        status?.let { criteria.add(Criteria.where("status").`is`(it.name)) }

        val query = Query().apply {
            if (criteria.isNotEmpty()) {
                addCriteria(Criteria().andOperator(*criteria.toTypedArray()))
            }
            with(pageable)
        }

        val results = mongoTemplate.find(query, PayoutTransaction::class.java)
        val total = mongoTemplate.count(query, PayoutTransaction::class.java)

        return PageImpl(results, pageable, total)
    }
}
package eu.kotlinBoilerplate.domain

import eu.kotlinBoilerplate.domain.exceptions.PayoutTransactionNotFoundException
import eu.kotlinBoilerplate.domain.model.PayoutTransaction
import eu.kotlinBoilerplate.domain.model.PayoutTransactionStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class PayoutService(
    private val payoutTransactionRepository: PayoutTransactionRepository,
    private val payoutTransactionCustomRepository: PayoutTransactionCustomRepository
) {
    fun getPayoutTransactionById(id: UUID, organizationCode: String, isSuperAdmin: Boolean?): PayoutTransaction {
        return if (isSuperAdmin == true) {
            payoutTransactionRepository.findById(id).orElseThrow { PayoutTransactionNotFoundException() }
        } else {
            payoutTransactionRepository.findByIdAndOrganizationCode(id, organizationCode)
                .orElseThrow { PayoutTransactionNotFoundException() }
        }
    }

    fun getPayoutTransactions(
        payoutTransactionId: UUID?,
        transferId: UUID?,
        fromDate: Instant?,
        toDate: Instant?,
        organizationCode: String?,
        status: PayoutTransactionStatus?,
        pageable: Pageable
    ): Page<PayoutTransaction> {
        return payoutTransactionCustomRepository.findAllByFilters(
            payoutTransactionId,
            transferId,
            fromDate,
            toDate,
            organizationCode,
            status,
            pageable
        )
    }

}

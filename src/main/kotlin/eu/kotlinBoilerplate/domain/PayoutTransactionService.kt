package eu.kotlinBoilerplate.domain

import eu.kotlinBoilerplate.domain.messaging.PayoutTransactionData
import eu.kotlinBoilerplate.domain.model.PayoutTransaction
import eu.kotlinBoilerplate.domain.messaging.PayoutTransactionSuccessEvent
import eu.kotlinBoilerplate.domain.model.PayoutTransactionStatus
import eu.kotlinBoilerplate.domain.model.Transfer
import eu.kotlinBoilerplate.logger
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class PayoutTransactionService(
    private val payoutTransactionRepository: PayoutTransactionRepository,
    private val publisher: PublisherService
) {
    fun processBankTransferPayout(transfer: Transfer){
        logger.info("Processing bank transfer payout for transfer: $transfer")

        // Create new PayoutTransaction
         val payoutTransaction = PayoutTransaction(
             id = UUID.randomUUID(),
             status = PayoutTransactionStatus.COMPLETED, // Assuming the status is completed for this example
             transfer = transfer,
             organizationCode = transfer.organizationCode,
             createdAt = Instant.now(),
             updatedAt = Instant.now()
         )
        payoutTransactionRepository.save(payoutTransaction)

        //tymczasowo wysylamy ze sie udało (zawsze)
        val event = PayoutTransactionSuccessEvent(
            organizationCode = transfer.organizationCode,
            metadata = PayoutTransactionData(
                id = payoutTransaction.id,
                transferId = transfer.id
            )
        )
        publisher.publish(event)

    }

    private val logger by logger()
}
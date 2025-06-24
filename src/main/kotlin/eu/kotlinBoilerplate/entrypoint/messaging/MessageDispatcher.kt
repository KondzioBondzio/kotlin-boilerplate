package eu.kotlinBoilerplate.entrypoint.messaging

import eu.kotlinBoilerplate.domain.PayoutTransactionService
import eu.kotlinBoilerplate.entrypoint.messaging.model.ProcessBankTransferPayoutCommand
import eu.kotlinBoilerplate.logger
import org.springframework.stereotype.Component

@Component
class MessageDispatcher(
    private val payoutTransactionService: PayoutTransactionService
) {
    fun dispatchBankTransferCreatedCommand(msg: ProcessBankTransferPayoutCommand){
        logger.info("Dispatching bank transfer created event: $msg")
        payoutTransactionService.processBankTransferPayout(msg.transfer)
    }

    private val logger by logger()
}

package eu.kotlinBoilerplate.entrypoint.messaging.model

import eu.kotlinBoilerplate.domain.messaging.BaseMessage
import eu.kotlinBoilerplate.domain.model.Transfer

data class ProcessBankTransferPayoutCommand(
    override val routingKey: String,
    override val organizationCode: String,
    val transfer: Transfer
) : BaseMessage
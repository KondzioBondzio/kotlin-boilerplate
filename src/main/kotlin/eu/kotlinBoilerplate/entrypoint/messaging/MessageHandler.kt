package eu.kotlinBoilerplate.entrypoint.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import eu.kotlinBoilerplate.entrypoint.messaging.model.ProcessBankTransferPayoutCommand
import org.springframework.amqp.core.Message
import eu.kotlinBoilerplate.logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class MessageHandler(
    private val dispatcher: MessageDispatcher,
    private val objectMapper: ObjectMapper
) {
    private val logger by logger()

    @RabbitListener(queues = ["payouts-queue"])
    fun handle(message: Message) {
        val routingKey = message.messageProperties.receivedRoutingKey
        val body = String(message.body)

        try {
            when (routingKey) {
                "payouts.command.processBankTransfer" -> {
                    val msg = objectMapper.readValue(body, ProcessBankTransferPayoutCommand::class.java)
                    logger.info("Bank Transfer created event: $msg")
                    dispatcher.dispatchBankTransferCreatedCommand(msg)
                }

            }

        } catch (e: Exception) {
            logger.error("Failed to process message: $body, error: ${e.message}")
        }
    }
}











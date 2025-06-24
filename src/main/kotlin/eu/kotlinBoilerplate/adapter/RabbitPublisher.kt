package eu.kotlinBoilerplate.adapter
import eu.kotlinBoilerplate.logger
import com.fasterxml.jackson.databind.ObjectMapper
import eu.kotlinBoilerplate.domain.PublisherService
import eu.kotlinBoilerplate.domain.messaging.BaseMessage
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RabbitPublisher(
    private val rabbitTemplate: RabbitTemplate,
    private val objectMapper: ObjectMapper
) : PublisherService {
    private val logger by logger()

    override fun publish(command: BaseMessage) {
        try {
            val body = objectMapper.writeValueAsBytes(command)
            val messageProperties = MessageProperties()
            messageProperties.contentType = MessageProperties.CONTENT_TYPE_JSON
            val message = Message(body, messageProperties)

            rabbitTemplate.convertAndSend("exchange", command.routingKey, message)
        } catch (e: Exception) {
            logger.error("Failed to publish message: ${e.message}")
        }
    }
}
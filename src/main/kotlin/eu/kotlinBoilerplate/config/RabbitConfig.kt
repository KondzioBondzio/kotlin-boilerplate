package eu.kotlinBoilerplate.config


import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitConfig {

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory, jsonMessageConverter: MessageConverter): RabbitTemplate {
        return RabbitTemplate(connectionFactory).also {
                template -> template.messageConverter = jsonMessageConverter
        }
    }

    @Bean
    fun queue() = Queue("payouts-queue", true)

    @Bean
    fun exchange() = TopicExchange("exchange", true, false)

    @Bean
    fun declarables(queue: Queue, exchange: TopicExchange): Declarables {
        val routingKeys = listOf("payouts.#")

        val bindings = routingKeys.map { routingKey ->
            BindingBuilder.bind(queue).to(exchange).with(routingKey)
        }

        return Declarables(queue, exchange, *bindings.toTypedArray())
    }

    @Bean
    fun jsonMessageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }


}

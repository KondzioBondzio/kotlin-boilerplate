package eu.kotlinBoilerplate.domain

import eu.kotlinBoilerplate.domain.messaging.BaseMessage


interface PublisherService {
    fun publish(command: BaseMessage)
}
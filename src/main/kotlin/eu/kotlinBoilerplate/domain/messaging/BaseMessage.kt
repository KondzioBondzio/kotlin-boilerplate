package eu.kotlinBoilerplate.domain.messaging

interface BaseMessage {
    val routingKey: String
    val organizationCode: String
}
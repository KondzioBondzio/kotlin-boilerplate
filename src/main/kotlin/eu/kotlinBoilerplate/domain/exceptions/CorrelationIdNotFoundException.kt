package eu.kotlinBoilerplate.domain.exceptions


class CorrelationIdNotFoundException : DomainException("Missing 'correlation_id' in the message")
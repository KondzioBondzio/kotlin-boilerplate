package eu.kotlinBoilerplate.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SecurityConfig {

    @Bean
    fun jwtTokenConverter(objectMapper: ObjectMapper) = JwtTokenConverter(objectMapper)
}
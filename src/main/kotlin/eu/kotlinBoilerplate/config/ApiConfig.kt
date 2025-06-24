package eu.kotlinBoilerplate.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class ApiConfig {

    @Bean
    fun mcClientsRestTemplate(mcClientsClientConfig: RestClientConfig): RestTemplate = createRestTemplate(mcClientsClientConfig)

    @Bean
    @ConfigurationProperties("http.ms-clients") // each client should have own restTemplate
    fun mcClientsClientConfig() = RestClientConfig()

    private fun createRestTemplate(mcClientsClientConfig: RestClientConfig) = RestTemplateBuilder()
        .setReadTimeout(mcClientsClientConfig.readTimeout!!.durationMillis())
        .setConnectTimeout(mcClientsClientConfig.connectionTimeout!!.durationMillis())
        // you can add interceptors here
        .build()
}

class RestClientConfig {
    var connectionTimeout: Long? = null
    var readTimeout: Long? = null
    var baseUrl: String? = null
}

private fun Long.durationMillis() = Duration.ofMillis(this)
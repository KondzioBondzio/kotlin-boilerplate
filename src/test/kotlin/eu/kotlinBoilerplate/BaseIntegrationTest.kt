package eu.kotlinBoilerplate

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [AppRunner::class, TestcontainersConfiguration::class]
)
@Testcontainers
@ActiveProfiles("integration")
abstract class BaseIntegrationTest {

    companion object {
        @JvmStatic
        @RegisterExtension
        var wmServer: WireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().port(8082).dynamicHttpsPort())
            .configureStaticDsl(true)
            .build()

        @Container
        var mongoDBContainer: MongoDBContainer = MongoDBContainer("mongo:6.0").withExposedPorts(27017)


        @DynamicPropertySource
        @JvmStatic
        fun containersProperties(registry: DynamicPropertyRegistry) {
            mongoDBContainer.start()
            registry.add("spring.data.mongodb.host") { mongoDBContainer.host }
            registry.add("spring.data.mongodb.port") { mongoDBContainer.firstMappedPort }
        }
    }

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @LocalServerPort
    var port: Int? = null

    fun localUrl(path: String): String = "http://localhost:$port$path"

    @AfterEach
    fun reset() {
//        val tables = listOf(
////            "nested_objects",
////            "samples"
//        )
//        jdbcTemplate.execute("truncate ${tables.joinToString()} cascade")
        wmServer.resetAll()
    }

}
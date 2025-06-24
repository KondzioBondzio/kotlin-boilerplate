package eu.kotlinBoilerplate

import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {


//	@Bean
//	@ServiceConnection
//	fun postgresContainer(): PostgreSQLContainer<*> {
//		return PostgreSQLContainer(DockerImageName.parse("mongodb:latest"))
//	}
}

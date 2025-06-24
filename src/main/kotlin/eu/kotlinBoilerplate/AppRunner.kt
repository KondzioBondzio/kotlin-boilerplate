package eu.kotlinBoilerplate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication(scanBasePackages = ["eu.kotlinBoilerplate"])
@ConfigurationPropertiesScan(basePackages = ["eu"])
@EnableMongoRepositories
class AppRunner {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<AppRunner>(*args)
		}
	}
}


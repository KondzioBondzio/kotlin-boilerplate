package eu.kotlinBoilerplate.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenApiConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        val securitySchemeName = "HTTPBearer"
        return OpenAPI()
            .info(
                Info()
                    .title("Kotlin-Boilerplate")
                    .version("0.1.0")
                    .description("")
            )
            .servers(
                listOf(
                    Server().url("http://localhost:8000").description("Local server"),
                    Server().url("http://localhost:8006").description("Local server")
                )
            )
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(
                Components()
                    .addSecuritySchemes(
                        securitySchemeName,
                        SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
    }
    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/api/v1/**")
            .build()
    }

    @Bean
    fun backofficeApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("backoffice")
            .pathsToMatch("/backoffice/**")
            .build()
    }
}

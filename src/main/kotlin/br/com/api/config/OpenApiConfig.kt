package br.com.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("RESTFUL API com Kotlin 1.6.110 e Spring Boot 3.0.0")
                    .version("V1")
                    .description("Some description about your API.")
                    .termsOfService("https://pub.carlos.com.br/api")
                    .license(
                        License().name("Apache 2.0")
                            .url("https://pub.carlos.com.br/api")
                    )
            )
    }
}
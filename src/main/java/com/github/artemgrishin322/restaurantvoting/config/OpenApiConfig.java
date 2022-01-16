package com.github.artemgrishin322.restaurantvoting.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = """
                        Graduation project. This API is designed for restaurant voting.
                        Users have an ability to vote for restaurant where they'd like to have lunch
                        """,
                contact = @Contact(url = "https://github.com/artemgrishin322", name = "Artem Grishin", email = "artem.grishin322@gmail.com")
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
public class OpenApiConfig {

        @Bean
        public GroupedOpenApi api() {
                return GroupedOpenApi.builder()
                        .group("REST API")
                        .pathsToMatch("/api/**")
                        .build();
        }
}

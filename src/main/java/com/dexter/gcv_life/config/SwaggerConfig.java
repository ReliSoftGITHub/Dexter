package com.dexter.gcv_life.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI GcvOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GCV API Documentation")
                        .description("This is API documentation for GCV")
                        .termsOfService("https://www.example.com/terms")
                        .version("1.0")
                        .license(new License().name("Apache")));

    }
}

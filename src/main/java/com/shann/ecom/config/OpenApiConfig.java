package com.shann.ecom.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce User Management API")
                        .description("API for managing users in the e-commerce platform, including registration, authentication, and profile management.")
                        .version("1.0"));
    }
}
package com.vyshnikova.propertyview.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI propertyViewOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Property View API")
                .description("REST API for browsing, searching and creating hotels")
                .version("1.0"));
    }
}
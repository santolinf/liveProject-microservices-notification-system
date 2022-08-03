package com.manning.application.notification.template.formatter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info().title("Notification Templates Service"));
    }
}

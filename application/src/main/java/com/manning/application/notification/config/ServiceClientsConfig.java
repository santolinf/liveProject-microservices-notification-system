package com.manning.application.notification.config;

import com.manning.application.notification.formatters.ClientsErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.manning.application.notification.integration")
public class ServiceClientsConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ClientsErrorDecoder();
    }
}

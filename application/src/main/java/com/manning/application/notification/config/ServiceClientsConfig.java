package com.manning.application.notification.config;

import com.manning.application.notification.formatters.ClientsErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The OpenFeign client is auto-integrated with service discovery.
 * Declare an interface with required methods for each client. Annotate the interface with @FeignClient
 * that points to the target service using its discovery name.
 */
@Configuration
@EnableFeignClients(basePackages = "com.manning.application.notification.integration")
public class ServiceClientsConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ClientsErrorDecoder();
    }
}

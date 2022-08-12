package com.manning.application.notification.config;

import com.manning.application.notification.formatters.ClientsErrorDecoder;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Note that the OpenFeign client is auto-integrated with service discovery.
 * Declare an interface with required methods for each client. Annotate the interface with @FeignClient
 * that points to the target service using its discovery name.
 * <br/><br/>
 * Here we need to define beans to convert <em>errors</em> and <em>responses</em> from the non-blocking reactive streams.
 */
@Configuration
@EnableFeignClients(basePackages = "com.manning.application.notification.integration")
public class ServiceClientsConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ClientsErrorDecoder();
    }

    private final ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;

    @Bean
    Encoder feignEncoder() {
        return new SpringEncoder(messageConverters);
    }

    @Bean
    Decoder feignDecoder() {
        return new SpringDecoder(messageConverters);
    }
}

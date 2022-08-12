package com.manning.application.notification.integration;

import com.manning.application.notification.common.model.NotificationGatewayRequest;
import com.manning.application.notification.common.model.NotificationGatewayResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.manning.application.notification.common.model.RemoteResponseStatus.WARNING;

/**
 * When the failure rate threshold is reached and the circuit id <em>OPEN</em> we fallback to returning a warning
 * message that the notification has not been sent.
 * As a fallback, we could try a different service, save the notification for processing later however we could not return a cached
 * response as notifications are generally unique.
 */
@FeignClient(name = "${com.manning.application.service.gateway.id}")
@CircuitBreaker(name = "gateway", fallbackMethod = "gatewayFallback")
public interface NotificationGatewayClient {

    @PostMapping("/api/notifications/send")
    NotificationGatewayResponse sendNotification(@RequestBody NotificationGatewayRequest request);

    default NotificationGatewayResponse gatewayFallback(NotificationGatewayRequest request, Throwable ex) {
        return NotificationGatewayResponse.builder()
                .status(WARNING)
                .statusDescription(
                        "Notification not sent. Underlying cause: "
                                + "[" + Thread.currentThread().getName() + "] "
                                + ex.getMessage()
                )
                .build();
    }
}

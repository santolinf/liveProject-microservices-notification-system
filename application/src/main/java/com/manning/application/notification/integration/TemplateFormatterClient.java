package com.manning.application.notification.integration;

import com.manning.application.notification.common.model.NotificationTemplateRequest;
import com.manning.application.notification.common.model.NotificationTemplateResponse;
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
@FeignClient(name = "${com.manning.application.service.template-formatter.id}")
@CircuitBreaker(name = "template-formatter", fallbackMethod = "templateFormatterFallback")
public interface TemplateFormatterClient {

    @PostMapping("/api/notifications/templates")
    NotificationTemplateResponse formatNotification(@RequestBody NotificationTemplateRequest request);

    default NotificationTemplateResponse templateFormatterFallback(NotificationTemplateRequest request, Throwable ex) {
        return NotificationTemplateResponse.builder()
                .status(WARNING)
                .statusDescription(
                        "Template service not available. Underlying cause: "
                        + "[" + Thread.currentThread().getName() + "] "
                        + ex.getMessage()
                )
                .build();
    }
}

package com.manning.application.notification.integration;

import com.manning.application.notification.common.model.NotificationPreferencesResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.manning.application.notification.common.model.RemoteResponseStatus.WARNING;

/**
 * When the failure rate threshold is reached and the circuit id <em>OPEN</em> we fallback to returning a warning
 * message that the notification has not been sent.
 * As a fallback, we could try a different service, save the notification for processing later however we could not return a cached
 * response as notifications are generally unique.
 */
@FeignClient(name = "${com.manning.application.service.preferences.id}")
@CircuitBreaker(name = "preferences", fallbackMethod = "preferencesFallback")
public interface PreferencesClient {

    @GetMapping("/notification/preferences")
    NotificationPreferencesResponse getPreferences(@RequestParam("customerId") String customerId);

    default NotificationPreferencesResponse preferencesFallback(String customerId, Throwable ex) {
        return NotificationPreferencesResponse.builder()
                .status(WARNING)
                .statusDescription(
                        "Preferences not available. Underlying cause: "
                                + "[" + Thread.currentThread().getName() + "] "
                                + ex.getMessage()
                )
                .build();
    }
}

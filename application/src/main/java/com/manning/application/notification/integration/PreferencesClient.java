package com.manning.application.notification.integration;

import com.manning.application.notification.common.model.NotificationPreferencesResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.manning.application.notification.common.model.RemoteResponseStatus.WARNING;

/**
 * When the failure rate threshold is reached and the circuit id <em>OPEN</em> we fallback to returning a warning
 * message that the notification has not been sent.
 * As a fallback, we could try a different service, save the notification for processing later however we could not return a cached
 * response as notifications are generally unique.
 * <br/><br/>
 * Circuit breaker with Retry
 * <br/><br/>
 * Due to Spring's method based AOP mechanism, by default, the retry mechanism has lower priority
 * to the circuit breaker aspect. Hence, <code>Retry  ( Circuit Breaker ( function ) )</code>
 * <br/><br/>
 * To change this, we need to add an aspect order in the properties file.
 * <br/><br/>
 * Also, by not having a <em>fallback</em> on the Retry we allow the Circuit Breaker to take over when the retries have
 * failed.
 */
@FeignClient(name = "${com.manning.application.service.preferences.id}")
@CircuitBreaker(name = "preferences", fallbackMethod = "preferencesFallback")
@Retry(name = "preferences")
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

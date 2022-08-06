package com.manning.application.notification.integration;

import com.manning.application.notification.common.model.NotificationPreferencesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "preferencesClient",
        url = "${com.manning.application.service.preferences.url}"
)
public interface PreferencesClient {

    @GetMapping("/notification/preferences")
    NotificationPreferencesResponse getPreferences(@RequestParam("customerId") String customerId);
}

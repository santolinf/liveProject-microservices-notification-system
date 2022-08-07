package com.manning.application.notification.integration;

import com.manning.application.notification.common.model.NotificationPreferencesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${com.manning.application.service.preferences.id}")
public interface PreferencesClient {

    @GetMapping("/notification/preferences")
    NotificationPreferencesResponse getPreferences(@RequestParam("customerId") String customerId);
}

package com.manning.application.notification.preferences.controllers;

import com.manning.application.notification.common.model.NotificationPreferencesRequest;
import com.manning.application.notification.common.model.NotificationPreferencesResponse;
import com.manning.application.notification.preferences.services.NotificationPreferencesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Notification Preferences API", description = "Manage customer notification preferences")
@RestController
@RequiredArgsConstructor
public class NotificationPreferencesController {

    private final NotificationPreferencesService service;

    @Operation(summary = "Get the notification preferences for a customer")
    @GetMapping(path = "/notification/preferences", produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationPreferencesResponse getPreferences(@Valid NotificationPreferencesRequest request) {
        return service.findNotificationPreferences(request);
    }
}

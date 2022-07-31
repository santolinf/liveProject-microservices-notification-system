package com.manning.application.notification.preferences.controllers;

import com.manning.application.notification.preferences.model.NotificationPreferencesRequest;
import com.manning.application.notification.preferences.model.NotificationPreferencesResponse;
import com.manning.application.notification.preferences.services.NotificationPreferencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class NotificationPreferencesController {

    private final NotificationPreferencesService service;

    @GetMapping(path = "/notification/preferences", produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationPreferencesResponse getPreferences(@Valid NotificationPreferencesRequest request) {
        return service.findNotificationPreferences(request);
    }
}

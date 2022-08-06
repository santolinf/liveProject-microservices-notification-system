package com.manning.application.notification.controllers;

import com.manning.application.notification.model.NotificationRequest;
import com.manning.application.notification.model.NotificationResponse;
import com.manning.application.notification.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Notification Application", description = "Accepts consuming channel application requests")
@RestController
@RequiredArgsConstructor
public class NotificationApplicationController {

    private final NotificationService service;

    @Operation(summary = "Accept notification requests")
    @PostMapping(path = "/api/notifications", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationResponse acceptNotification(@Valid @RequestBody NotificationRequest request) {
        return service.sendNotification(request);
    }
}

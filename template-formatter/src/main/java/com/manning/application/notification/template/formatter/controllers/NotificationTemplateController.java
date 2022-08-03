package com.manning.application.notification.template.formatter.controllers;

import com.manning.application.notification.template.formatter.model.NotificationTemplateRequest;
import com.manning.application.notification.template.formatter.model.NotificationTemplateResponse;
import com.manning.application.notification.template.formatter.services.NotificationTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Notification Template API", description = "Formats customer notifications using either SMS or Email templates")
@RestController
@RequiredArgsConstructor
public class NotificationTemplateController {

    private final NotificationTemplateService service;

    @Operation(summary = "Formats customer notification using a template")
    @PostMapping(path = "/api/notifications/templates", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationTemplateResponse formatNotification(@Valid @RequestBody NotificationTemplateRequest request) {
        return service.formatNotification(request);
    }
}

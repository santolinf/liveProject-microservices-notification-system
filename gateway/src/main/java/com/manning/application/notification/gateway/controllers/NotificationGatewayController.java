package com.manning.application.notification.gateway.controllers;

import com.manning.application.notification.common.model.NotificationGatewayRequest;
import com.manning.application.notification.common.model.NotificationGatewayResponse;
import com.manning.application.notification.gateway.services.NotificationGatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Notification Gateway API", description = "Forwards customer notifications onto either SMS or Email external provider gateways")
@RestController
@RequiredArgsConstructor
public class NotificationGatewayController {

    private final NotificationGatewayService service;

    @Operation(summary = "Forwards customer notifications as either Email or SMS")
    @PostMapping(path = "/api/notifications/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public NotificationGatewayResponse sendNotification(@Valid @RequestBody NotificationGatewayRequest request) {
        return service.sendNotification(request);
    }
}

package com.manning.application.notification.integration;

import com.manning.application.notification.common.model.NotificationGatewayRequest;
import com.manning.application.notification.common.model.NotificationGatewayResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "notificationGatewayClient",
        url = "${com.manning.application.service.gateway.url}"
)
public interface NotificationGatewayClient {

    @PostMapping("/api/notifications/send")
    NotificationGatewayResponse sendNotification(@RequestBody NotificationGatewayRequest request);
}

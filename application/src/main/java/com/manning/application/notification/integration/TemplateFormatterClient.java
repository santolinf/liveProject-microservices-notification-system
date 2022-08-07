package com.manning.application.notification.integration;

import com.manning.application.notification.common.model.NotificationTemplateRequest;
import com.manning.application.notification.common.model.NotificationTemplateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${com.manning.application.service.template-formatter.id}")
public interface TemplateFormatterClient {

    @PostMapping("/api/notifications/templates")
    NotificationTemplateResponse formatNotification(@RequestBody NotificationTemplateRequest request);
}

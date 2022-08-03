package com.manning.application.notification.template.formatter.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class NotificationTemplateResponse {

    private String status;
    private String statusDescription;
    private String smsContent;
    private String emailContent;
    private String emailSubject;
}

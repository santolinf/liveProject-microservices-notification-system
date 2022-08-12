package com.manning.application.notification.common.model;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationTemplateResponse {

    private RemoteResponseStatus status;
    private String statusDescription;
    private String smsContent;
    private String emailContent;
    private String emailSubject;
}

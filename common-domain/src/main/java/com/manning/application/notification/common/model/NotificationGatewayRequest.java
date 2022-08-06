package com.manning.application.notification.common.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class NotificationGatewayRequest {

    @NotNull
    private String customerId;

    @NotNull
    private NotificationMode notificationMode;

    @NotNull
    private String notificationContent;

    private String emailAddress;

    private String emailSubject;

    private String phoneNumber;
}

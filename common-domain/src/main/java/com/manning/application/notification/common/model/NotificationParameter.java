package com.manning.application.notification.common.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NotificationParameter {

    private String notificationParameterName;
    private String notificationParameterValue;
}

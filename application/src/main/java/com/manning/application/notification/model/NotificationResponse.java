package com.manning.application.notification.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class NotificationResponse implements Serializable {

    private String status;
    private String statusDescription;
    private long notificationReferenceId;
}

package com.manning.application.notification.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Data
@ToString
public class NotificationResponse implements Serializable {

    private String status;
    private String statusDescription;
    private long notificationReferenceId;
}

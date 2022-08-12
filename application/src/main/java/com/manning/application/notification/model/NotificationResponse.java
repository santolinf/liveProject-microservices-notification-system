package com.manning.application.notification.model;

import com.manning.application.notification.common.model.RemoteResponseStatus;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Data
@ToString
public class NotificationResponse implements Serializable {

    private RemoteResponseStatus status;
    private String statusDescription;
    private long notificationReferenceId;
}

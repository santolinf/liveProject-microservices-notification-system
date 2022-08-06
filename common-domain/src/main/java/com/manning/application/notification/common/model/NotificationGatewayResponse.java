package com.manning.application.notification.common.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Data
@ToString
public class NotificationGatewayResponse implements Serializable {

    private String status;

    private String statusDescription;
}

package com.manning.application.notification.common.model;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationGatewayResponse implements Serializable {

    private RemoteResponseStatus status;

    private String statusDescription;
}

package com.manning.application.notification.gateway.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class NotificationGatewayRequest {

    @NotNull
    private String customerId;
}

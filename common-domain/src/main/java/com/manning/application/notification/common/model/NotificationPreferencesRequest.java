package com.manning.application.notification.common.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class NotificationPreferencesRequest {

    @NotNull
    private String customerId;
}

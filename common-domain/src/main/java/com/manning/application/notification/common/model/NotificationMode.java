package com.manning.application.notification.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum NotificationMode {

    SMS,
    EMAIL;

    @JsonCreator
    public static NotificationMode forValue(String value) {
        try {
            return NotificationMode.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Illegal Notification mode. Valid options are: " + Arrays.stream(NotificationMode.values()).map(Enum::name).collect(Collectors.joining(", "))
            );
        }
    }
}

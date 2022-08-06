package com.manning.application.notification.common.exceptions;

import lombok.Getter;

@Getter
public class NotificationServiceApiException extends RuntimeException {

    private final String requestUrl;
    private final int responseStatus;
    private final String responseBody;

    public NotificationServiceApiException(String requestUrl, int responseStatus, String responseBody) {
        super(responseBody);
        this.requestUrl = requestUrl;
        this.responseStatus = responseStatus;
        this.responseBody = responseBody;
    }
}

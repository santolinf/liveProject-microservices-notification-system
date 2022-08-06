package com.manning.application.notification.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    public enum Code {
        NOT_FOUND,
        VALIDATION_ERROR,
        UNSUPPORTED_OPERATION,
        SERVICE_ERROR,
        NOTIFICATION_ERROR
    }

    private Code code;
    private String message;
    private String data;
    private LocalDateTime timestamp;
}

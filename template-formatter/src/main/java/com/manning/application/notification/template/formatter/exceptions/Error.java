package com.manning.application.notification.template.formatter.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {

    enum Code {
        VALIDATION_ERROR
    }

    private Code code;
    private String message;
    private String data;
    private LocalDateTime timestamp;
}
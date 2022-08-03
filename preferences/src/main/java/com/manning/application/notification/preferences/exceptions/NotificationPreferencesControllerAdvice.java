package com.manning.application.notification.preferences.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static com.manning.application.notification.preferences.exceptions.Error.Code.NOT_FOUND;
import static com.manning.application.notification.preferences.exceptions.Error.Code.VALIDATION_ERROR;

@ControllerAdvice
public class NotificationPreferencesControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotificationPreferencesNotFoundException.class)
    public ResponseEntity<Error> handleNotificationPreferencesNotFoundException(NotificationPreferencesNotFoundException ex) {
        var error = Error.builder()
                .code(NOT_FOUND)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var message = ex.getFieldErrors().stream().map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining("; "));
        var error = Error.builder()
                .code(VALIDATION_ERROR)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, headers, status);
    }
}

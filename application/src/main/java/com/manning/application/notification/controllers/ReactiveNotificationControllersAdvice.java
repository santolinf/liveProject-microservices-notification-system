package com.manning.application.notification.controllers;

import com.manning.application.notification.common.exceptions.ErrorResponse;
import com.manning.application.notification.common.exceptions.NotificationNotFoundException;
import com.manning.application.notification.common.exceptions.NotificationServiceApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static com.manning.application.notification.common.exceptions.ErrorResponse.Code.*;

/**
 * We can use <code>@ExceptionHandler</code> annotated methods to handle errors that happen within the execution
 * of a WebFlux handler (e.g., controller method).
 * Unlike MVC we cannot handle errors happening during the mapping phase.
 */
@ControllerAdvice
public class ReactiveNotificationControllersAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        var error = ErrorResponse.builder()
                .code(VALIDATION_ERROR)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        var error = ErrorResponse.builder()
                .code(SERVICE_ERROR)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBindException(WebExchangeBindException ex) {
        var message = ex.getFieldErrors().stream().map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining("; "));
        var error = ErrorResponse.builder()
                .code(VALIDATION_ERROR)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        var error = ErrorResponse.builder()
                .code(UNSUPPORTED_OPERATION)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotificationNotFoundException(NotificationNotFoundException ex) {
        var error = ErrorResponse.builder()
                .code(NOT_FOUND)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotificationServiceApiException.class)
    public ResponseEntity<ErrorResponse> handleNotificationServiceApiException(NotificationServiceApiException ex) {
        var error = ErrorResponse.builder()
                .code(NOTIFICATION_ERROR)
                .message(ex.getResponseBody())
                .data(ex.getRequestUrl())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.resolve(ex.getResponseStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        var error = ErrorResponse.builder()
                .code(SERVICE_ERROR)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

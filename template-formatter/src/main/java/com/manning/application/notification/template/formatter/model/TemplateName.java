package com.manning.application.notification.template.formatter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum TemplateName {

    PhoneNumberChanged("Your Phone number has changed"),
    ViewBalance("Your Bank Balance");

    @Getter
    private final String subject;

    @JsonCreator
    public static TemplateName forValue(String value) {
        try {
            return TemplateName.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Illegal Template name. Valid options are: " + Arrays.stream(TemplateName.values()).map(Enum::name).collect(Collectors.joining(", "))
            );
        }
    }
}

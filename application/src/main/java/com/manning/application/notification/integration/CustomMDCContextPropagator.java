package com.manning.application.notification.integration;

import io.github.resilience4j.core.ContextPropagator;
import org.slf4j.MDC;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Not used. It is here only for documentation.
 */
public class CustomMDCContextPropagator implements ContextPropagator<Map<String, String>> {

    @Override
    public Supplier<Optional<Map<String, String>>> retrieve() {
        return () -> Optional.ofNullable(MDC.getCopyOfContextMap());
    }

    @Override
    public Consumer<Optional<Map<String, String>>> copy() {
        return (copyOfContextMap) -> {
            copyOfContextMap.ifPresent(MDC::setContextMap);
        };
    }

    @Override
    public Consumer<Optional<Map<String, String>>> clear() {
        return (contextMap) -> {
            MDC.clear();
        };
    }
}

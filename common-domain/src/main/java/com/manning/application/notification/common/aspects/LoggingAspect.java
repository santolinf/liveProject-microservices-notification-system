package com.manning.application.notification.common.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Aspect
public class LoggingAspect {

    @Around("within(com.manning.application.notification..services..*) && execution(public * *(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = getMethodName(joinPoint);
        String args = getArgs(joinPoint);
        log.info("Entering '{}' {}", methodName, args);
        Object result = joinPoint.proceed();
        log.info("Exiting '{}'", methodName);
        return result;
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }

    private String getArgs(ProceedingJoinPoint joinPoint) {
        return Stream.of(joinPoint.getArgs()).map(Object::toString).collect(Collectors.joining(", "));
    }
}

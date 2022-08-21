package com.manning.application.notification.common.autoconfiguration;

import com.manning.application.notification.common.aspects.LoggingAspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@AutoConfiguration
@ConditionalOnClass(Pointcut.class)
@EnableAspectJAutoProxy
public class CommonAspectsConfig {

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}

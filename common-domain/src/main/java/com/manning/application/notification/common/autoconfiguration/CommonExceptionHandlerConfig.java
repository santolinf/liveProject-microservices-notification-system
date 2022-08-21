package com.manning.application.notification.common.autoconfiguration;

import com.manning.application.notification.common.exceptions.NotificationControllersAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ConditionalOnMissingBean({ NotificationControllersAdvice.class })
@ComponentScan("com.manning.application.notification.common.exceptions")
public class CommonExceptionHandlerConfig {
}

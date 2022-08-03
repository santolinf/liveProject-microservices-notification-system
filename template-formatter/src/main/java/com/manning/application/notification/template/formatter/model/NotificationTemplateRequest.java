package com.manning.application.notification.template.formatter.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString
public class NotificationTemplateRequest {

    @NotEmpty
    private List<NotificationParameter> notificationParameters;
    @NotNull
    private TemplateName notificationTemplateName;
    @NotNull
    private NotificationMode notificationMode;
}

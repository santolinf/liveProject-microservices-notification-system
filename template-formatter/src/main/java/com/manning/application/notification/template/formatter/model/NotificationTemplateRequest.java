package com.manning.application.notification.template.formatter.model;

import com.manning.application.notification.common.model.NotificationMode;
import com.manning.application.notification.common.model.TemplateName;
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

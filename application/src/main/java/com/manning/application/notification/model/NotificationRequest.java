package com.manning.application.notification.model;

import com.manning.application.notification.common.model.NotificationParameter;
import com.manning.application.notification.common.model.TemplateName;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString
public class NotificationRequest {

    @NotNull
    private String customerId;

    @NotEmpty
    private List<NotificationParameter> notificationParameters;

    @NotNull
    private TemplateName notificationTemplateName;
}

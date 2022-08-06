package com.manning.application.notification.formatters;

import com.manning.application.notification.common.model.NotificationParameter;
import com.manning.application.notification.entities.Notification;
import com.manning.application.notification.entities.Parameter;
import com.manning.application.notification.model.NotificationRequest;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "notificationMode", ignore = true)
    Notification notificationRequestToNotification(NotificationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "parameterName", source = "notificationParameterName")
    @Mapping(target = "parameterValue", source = "notificationParameterValue")
    Parameter notificationParameterToParameter(NotificationParameter parameter);
}

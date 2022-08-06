package com.manning.application.notification.preferences.formatters;

import com.manning.application.notification.preferences.entities.NotificationPreferences;
import com.manning.application.notification.common.model.NotificationPreferencesResponse;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface NotificationPreferencesMapper {

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "statusDescription", ignore = true)
    NotificationPreferencesResponse notificationPreferencesToNotificationPreferencesResponse(
            NotificationPreferences notificationPreferences
    );
}

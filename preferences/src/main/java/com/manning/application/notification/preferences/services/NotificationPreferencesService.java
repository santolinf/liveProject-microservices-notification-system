package com.manning.application.notification.preferences.services;

import com.manning.application.notification.common.exceptions.NotificationNotFoundException;
import com.manning.application.notification.preferences.entities.NotificationPreferences;
import com.manning.application.notification.preferences.formatters.NotificationPreferencesMapper;
import com.manning.application.notification.common.model.NotificationPreferencesRequest;
import com.manning.application.notification.common.model.NotificationPreferencesResponse;
import com.manning.application.notification.preferences.repositories.NotificationPreferencesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.manning.application.notification.common.model.RemoteResponseStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class NotificationPreferencesService {

    private final NotificationPreferencesRepository repository;

    private final NotificationPreferencesMapper mapper;

    public NotificationPreferencesResponse findNotificationPreferences(NotificationPreferencesRequest request) {
        NotificationPreferences notificationPreferences =
                repository.findByCustomerId(request.getCustomerId())
                        .orElseThrow(() -> new NotificationNotFoundException(
                                "Notification preferences not found for customer id: " + request.getCustomerId()
                        )
                );

        NotificationPreferencesResponse response =
                mapper.notificationPreferencesToNotificationPreferencesResponse(notificationPreferences);
        response.setStatus(SUCCESS);
        response.setStatusDescription("Notification Received Successfully");
        return response;
    }
}

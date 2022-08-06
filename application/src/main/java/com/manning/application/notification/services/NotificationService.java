package com.manning.application.notification.services;

import com.manning.application.notification.common.model.*;
import com.manning.application.notification.entities.Notification;
import com.manning.application.notification.formatters.NotificationMapper;
import com.manning.application.notification.integration.NotificationGatewayClient;
import com.manning.application.notification.integration.PreferencesClient;
import com.manning.application.notification.integration.TemplateFormatterClient;
import com.manning.application.notification.model.NotificationRequest;
import com.manning.application.notification.model.NotificationResponse;
import com.manning.application.notification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.manning.application.notification.common.model.NotificationMode.EMAIL;
import static com.manning.application.notification.common.model.NotificationMode.SMS;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    private final NotificationMapper mapper;

    private final PreferencesClient preferencesClient;

    private final TemplateFormatterClient templateFormatterClient;

    private final NotificationGatewayClient notificationGatewayClient;

    @Transactional
    public NotificationResponse sendNotification(NotificationRequest request) {
        Notification notification = mapper.notificationRequestToNotification(request);
        notification = repository.save(notification);

        NotificationPreferencesResponse preferencesResponse = getPreferences(request.getCustomerId());

        NotificationMode notificationMode;
        if (preferencesResponse.isEmailPreferenceFlag()) {
            notificationMode = EMAIL;
        } else {
            notificationMode = SMS;
        }
        notification.setNotificationMode(notificationMode);

        NotificationTemplateResponse templateResponse = formatNotification(request, notificationMode);

        NotificationGatewayResponse gatewayResponse = sendNotification(
                request.getCustomerId(),
                preferencesResponse,
                notificationMode,
                templateResponse
        );

        NotificationResponse response = new NotificationResponse();
        response.setStatus(gatewayResponse.getStatus());
        response.setStatusDescription(gatewayResponse.getStatusDescription());
        response.setNotificationReferenceId(notification.getId());
        return response;
    }

    private NotificationGatewayResponse sendNotification(
            String customerId,
            NotificationPreferencesResponse preferencesResponse,
            NotificationMode notificationMode,
            NotificationTemplateResponse templateResponse
    ) {
        NotificationGatewayRequest gatewayRequest = new NotificationGatewayRequest();
        gatewayRequest.setNotificationMode(notificationMode);
        gatewayRequest.setCustomerId(customerId);

        if (EMAIL.equals(notificationMode)) {
            gatewayRequest.setEmailAddress(preferencesResponse.getEmailAddress());
            gatewayRequest.setEmailSubject(templateResponse.getEmailSubject());
            gatewayRequest.setNotificationContent(templateResponse.getEmailContent());
        } else { // SMS
            gatewayRequest.setPhoneNumber(preferencesResponse.getPhoneNumber());
            gatewayRequest.setNotificationContent(templateResponse.getSmsContent());
        }

        return notificationGatewayClient.sendNotification(gatewayRequest);
    }

    private NotificationTemplateResponse formatNotification(
            NotificationRequest request,
            NotificationMode notificationMode
    ) {
        NotificationTemplateRequest templateRequest = new NotificationTemplateRequest();
        templateRequest.setNotificationTemplateName(request.getNotificationTemplateName());
        templateRequest.setNotificationParameters(request.getNotificationParameters());
        templateRequest.setNotificationMode(notificationMode);

        return templateFormatterClient.formatNotification(templateRequest);
    }

    private NotificationPreferencesResponse getPreferences(String customerId) {
        return preferencesClient.getPreferences(customerId);
    }
}

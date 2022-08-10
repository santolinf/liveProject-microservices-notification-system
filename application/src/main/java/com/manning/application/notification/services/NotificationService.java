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
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    /**
     * The rationale for placing a circuit breaker here rather than on each individual clients {@link PreferencesClient},
     * {@link TemplateFormatterClient} and {@link NotificationGatewayClient}, is simply that all the clients are required for
     * sending a notification. For example, a notification cannot be sent without it being formatted with a template, and
     * the notification must be sent by a provider via a gateway.
     * When the failure rate threshold is reached and the circuit id <em>OPEN</em> we fallback to returning a warning
     * message that the notification has not been sent.
     * As a fallback, we could try a different service, save the notification for processing later however we could not return a cached
     * response as notifications are generally unique.
     */
    @Transactional
    @CircuitBreaker(name = "sendNotification", fallbackMethod = "sendNotificationFallback")
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

        return NotificationResponse.builder()
                .status(gatewayResponse.getStatus())
                .statusDescription(gatewayResponse.getStatusDescription())
                .notificationReferenceId(notification.getId())
                .build();
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

    NotificationResponse sendNotificationFallback(NotificationRequest request, RuntimeException ex) {
        return NotificationResponse.builder()
                .status("WARNING")
                .statusDescription("Notification not sent. Underlying cause: [" + ex.getMessage() + "]")
                .notificationReferenceId(-1)
                .build();
    }
}

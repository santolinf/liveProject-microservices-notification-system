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
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.concurrent.CompletableFuture;

import static com.manning.application.notification.common.model.NotificationMode.EMAIL;
import static com.manning.application.notification.common.model.NotificationMode.SMS;
import static com.manning.application.notification.common.model.RemoteResponseStatus.SUCCESS;
import static com.manning.application.notification.common.model.RemoteResponseStatus.WARNING;
import static io.github.resilience4j.bulkhead.annotation.Bulkhead.Type.THREADPOOL;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    private final NotificationMapper mapper;

    private final PreferencesClient preferencesClient;

    private final TemplateFormatterClient templateFormatterClient;

    private final NotificationGatewayClient notificationGatewayClient;

    /**
     * <em>Note</em>: I have placed the <code>@Bulkhead</code> annotation here, primarily because the integration clients
     * are implemented by <em>Feign</em> and Feign does not support non-blocking I/O.
     * See {@linkplain https://github.com/OpenFeign/feign/issues/361}.
     * <br/><br/>
     * I have decided to use thread-pool based bulkhead and this requires <code>CompletableFuture</code>s.
     * However, if we choose to have semaphore based bulkheads the integration clients can support it.
     * <br/><br/>
     * With <em>SEMAPHORE</em> based Bulkheads we can only control the number of concurrent requests and wait times,
     * and requests execute using the current threads of the incoming requests.
     * <code>maxWaitDuration</code> and <code>maxConcurrentCalls</code> belong to the Semaphore-based Bulkhead.
     * <br/><br/>
     * <em>THREADPOOL</em> based Bulkheads execute in separate threads and are limited by the thread pool capacity.
     * <code>maxThreadPoolSize</code>, <code>coreThreadPoolSize</code> and <code>queueCapacity</code> belong to the
     * Threadpool-based Bulkhead.
     */
    @Bulkhead(name = "sendNotification", type = THREADPOOL, fallbackMethod = "concurrentSendNotificationFallback")
    @Transactional
    public CompletableFuture<NotificationResponse> sendNotification(NotificationRequest request) {
        Notification notification = mapper.notificationRequestToNotification(request);
        notification = repository.save(notification);

        NotificationPreferencesResponse preferencesResponse = getPreferences(request.getCustomerId());
        if (!SUCCESS.equals(preferencesResponse.getStatus())) {
            return toWarningNotificationResponse(preferencesResponse.getStatusDescription());
        }

        NotificationMode notificationMode;
        if (preferencesResponse.isEmailPreferenceFlag()) {
            notificationMode = EMAIL;
        } else {
            notificationMode = SMS;
        }
        notification.setNotificationMode(notificationMode);

        NotificationTemplateResponse templateResponse = formatNotification(request, notificationMode);
        if (!SUCCESS.equals(templateResponse.getStatus())) {
            return toWarningNotificationResponse(templateResponse.getStatusDescription());
        }

        NotificationGatewayResponse gatewayResponse = sendNotification(
                request.getCustomerId(),
                preferencesResponse,
                notificationMode,
                templateResponse
        );
        if (!SUCCESS.equals(gatewayResponse.getStatus())) {
            return toWarningNotificationResponse(gatewayResponse.getStatusDescription());
        }

        return CompletableFuture.completedFuture(NotificationResponse.builder()
                .status(gatewayResponse.getStatus())
                .statusDescription(gatewayResponse.getStatusDescription())
                .notificationReferenceId(notification.getId())
                .build());
    }

    private CompletableFuture<NotificationResponse> toWarningNotificationResponse(String statusDescription) {
        return CompletableFuture.completedFuture(NotificationResponse.builder()
                .status(WARNING)
                .statusDescription(statusDescription)
                .notificationReferenceId(-1)
                .build());
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

    CompletableFuture<NotificationResponse> concurrentSendNotificationFallback(NotificationRequest request, Throwable ex) {
        return CompletableFuture.completedFuture(NotificationResponse.builder()
                .status(WARNING)
                .statusDescription("Notification not sent. Underlying cause: [" + Thread.currentThread().getId() + "/" + Thread.currentThread().getName() + "; " + ex.getMessage() + "]")
                .notificationReferenceId(-2)
                .build());
    }
}

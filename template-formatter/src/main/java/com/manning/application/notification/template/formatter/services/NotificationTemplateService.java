package com.manning.application.notification.template.formatter.services;

import com.manning.application.notification.template.formatter.model.NotificationParameter;
import com.manning.application.notification.template.formatter.model.NotificationTemplateRequest;
import com.manning.application.notification.template.formatter.model.NotificationTemplateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.manning.application.notification.template.formatter.model.NotificationMode.EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTemplateService {

    private final TemplateEngine templateEngine;

    private final TemplateLoader templateLoader;

    @PostConstruct
    void setupTemplateEngine() {
        templateEngine.setTemplateResolver(new StringTemplateResolver());
    }

    public NotificationTemplateResponse formatNotification(NotificationTemplateRequest request) {
        NotificationTemplateResponse.NotificationTemplateResponseBuilder response;
        if (EMAIL.equals(request.getNotificationMode())) {
            response = formatEmailNotification(request);
        } else {
            response = formatSmsNotification(request);
        }

        return response.status("SUCCESS")
                .statusDescription("Successfully merged the template with the template parameters")
                .build();
    }

    private NotificationTemplateResponse.NotificationTemplateResponseBuilder formatEmailNotification(
            NotificationTemplateRequest request
    ) {
        var template = templateLoader.loadEmailTemplate(request.getNotificationTemplateName());

        var context = new Context();
        context.setVariables(toMap(request.getNotificationParameters()));

        String result = templateEngine.process(template, context);

        return NotificationTemplateResponse.builder()
                .emailContent(result)
                .emailSubject(request.getNotificationTemplateName().getSubject());
    }

    private NotificationTemplateResponse.NotificationTemplateResponseBuilder formatSmsNotification(
            NotificationTemplateRequest request
    ) {
        var template = templateLoader.loadSmsTemplate(request.getNotificationTemplateName());

        var result = StringSubstitutor.replace(template, toMap(request.getNotificationParameters()));

        return NotificationTemplateResponse.builder()
                .smsContent(result);
    }

    private Map<String, Object> toMap(List<NotificationParameter> notificationParameters) {
        return notificationParameters.stream().collect(Collectors.toMap(
                NotificationParameter::getNotificationParameterName,
                NotificationParameter::getNotificationParameterValue)
        );
    }
}

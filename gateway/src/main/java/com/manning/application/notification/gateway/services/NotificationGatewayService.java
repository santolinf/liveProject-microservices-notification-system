package com.manning.application.notification.gateway.services;

import com.manning.application.notification.gateway.config.StubNotificationSendersConfig;
import com.manning.application.notification.common.model.NotificationGatewayRequest;
import com.manning.application.notification.common.model.NotificationGatewayResponse;
import com.manning.application.notification.gateway.services.mail.StubMailSender;
import com.manning.application.notification.gateway.services.sms.StubSmsSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.manning.application.notification.common.model.NotificationMode.EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationGatewayService {

    private final static boolean contentIsHtml = true;

    private final StubNotificationSendersConfig.LogoConfig logoConfig;
    private final StubNotificationSendersConfig.EmailConfig emailConfig;
    private final StubNotificationSendersConfig.SmsConfig smsConfig;

    private final StubMailSender mailSender;

    private final StubSmsSender smsSender;

    @PostConstruct
    public void initSmsSender() {
        smsSender.init();
    }

    public NotificationGatewayResponse sendNotification(NotificationGatewayRequest request) {
        NotificationGatewayResponse.NotificationGatewayResponseBuilder response;
        if (EMAIL.equals(request.getNotificationMode())) {
            sendEmailNotification(request);
        } else {
            sendSmsNotification(request);
        }

        return NotificationGatewayResponse.builder()
                .status("SUCCESS")
                .statusDescription("Notification Received Successfully")
                .build();
    }

    public void sendEmailNotification(NotificationGatewayRequest request) {
        assertEmailSubjectAndRecipient(request);

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.addAttachment(
                    logoConfig.getFilename(),
                    new ClassPathResource(logoConfig.getFilename())
            );

            helper.setFrom(emailConfig.getFrom());
            helper.setTo(request.getEmailAddress());
            helper.setSubject(request.getEmailSubject());
            helper.setText(request.getNotificationContent(), contentIsHtml);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendSmsNotification(NotificationGatewayRequest request) {
        assertPhoneNumber(request);

        StubSmsSender.Message message = StubSmsSender.Message.creator(
                        request.getPhoneNumber(),
                        smsConfig.getFromPhoneNumber(),
                        request.getNotificationContent()
                )
                .create();

        smsSender.send(message);
        log.info("SMS message sent [SID: {}]", message.getSid());
    }

    private void assertPhoneNumber(NotificationGatewayRequest request) {
        if (isNullOrEmpty(request.getPhoneNumber())) {
            throw new IllegalArgumentException("phoneNumber: must not be null");
        }
    }

    private void assertEmailSubjectAndRecipient(NotificationGatewayRequest request) {
        String requiredEmailValues = Stream.of(
                    (isNullOrEmpty(request.getEmailAddress()) ? "emailAddress: must not be null" : null),
                    (isNullOrEmpty(request.getEmailSubject()) ? "emailSubject: must not be null" : null)
                ).filter(s -> !isNullOrEmpty(s))
                .collect(Collectors.joining("; "));
        if (!isNullOrEmpty(requiredEmailValues)) {
            throw new IllegalArgumentException(requiredEmailValues);
        }
    }
}

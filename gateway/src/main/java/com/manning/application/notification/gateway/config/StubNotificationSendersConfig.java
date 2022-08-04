package com.manning.application.notification.gateway.config;

import com.manning.application.notification.gateway.services.sms.StubSmsSender;
import com.manning.application.notification.gateway.services.mail.StubMailSender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class StubNotificationSendersConfig {

    @Getter
    @Setter
    public static class LogoConfig {

        private String filename;
    }

    @Getter
    @Setter
    public static class EmailConfig {

        private String from;
    }

    @Getter
    @Setter
    public static class SmsConfig {

        private String accountSid;
        private String authToken;
        private String fromPhoneNumber;
    }

    @Bean
    @ConfigurationProperties(prefix = "com.manning.application.config.logo")
    public LogoConfig notificationLogoConfig() {
        return new LogoConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "com.manning.application.config.email")
    public EmailConfig notificationEmailConfig() {
        return new EmailConfig();
    }

    @Bean
    public MailProperties mailProperties() {
        return new MailProperties();
    }

    @Bean
    public StubMailSender stubMailSender() {
        StubMailSender mailSender = new StubMailSender();
        mailSender.setHost(mailProperties().getHost());
        mailSender.setPort(mailProperties().getPort());
        mailSender.setUsername(mailProperties().getUsername());
        mailSender.setPassword(mailProperties().getPassword());
        mailSender.setProtocol(mailProperties().getProtocol());
        mailSender.setDefaultEncoding(mailProperties().getDefaultEncoding());

        return mailSender;
    }

    @Bean
    @ConfigurationProperties(prefix = "com.manning.application.config.sms")
    public SmsConfig notificationSmsConfig() {
        return new SmsConfig();
    }

    @Bean
    public StubSmsSender stubSmsSender() {
        StubSmsSender smsSender = new StubSmsSender();
        smsSender.setAccountSid(notificationSmsConfig().getAccountSid());
        smsSender.setAuthToken(notificationSmsConfig().getAuthToken());
        smsSender.setFromPhoneNumber(notificationSmsConfig().getFromPhoneNumber());

        return smsSender;
    }
}

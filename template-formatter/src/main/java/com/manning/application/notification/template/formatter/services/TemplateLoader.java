package com.manning.application.notification.template.formatter.services;

import com.manning.application.notification.common.model.TemplateName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import static com.manning.application.notification.common.model.TemplateName.PhoneNumberChanged;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
public class TemplateLoader {

    @Value("classpath:templates/email/PhoneNumberChanged.html")
    private Resource phoneNumberChangedTemplateFile;

    @Value("classpath:templates/email/ViewBalance.html")
    private Resource viewBalanceTemplateFile;

    @Cacheable(value = "emailTemplates")
    public String loadEmailTemplate(TemplateName templateName) {
        log.info("loading email template: {}", templateName.name());

        try {
            if (PhoneNumberChanged.equals(templateName)) {
                return asString(phoneNumberChangedTemplateFile);
            } else {
                return asString(viewBalanceTemplateFile);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String loadSmsTemplate(TemplateName templateName) {
        if (PhoneNumberChanged.equals(templateName)) {
            return "Hello ${name}\nWelcome to the Citizen Bank\n"
                    + "Your Phone number is changed from ${oldPhoneNumber} to ${newPhoneNumber}\n";
        } else {
            return "Hello ${name}\nWelcome to the Citizen Bank\n"
                    + "Your balance is ${balance}\nThanks";
        }
    }

    private String asString(Resource file) throws IOException {
        try (Reader in = new InputStreamReader(file.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(in);
        }
    }
}

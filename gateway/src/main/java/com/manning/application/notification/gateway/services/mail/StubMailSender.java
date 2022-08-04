package com.manning.application.notification.gateway.services.mail;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.isNull;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Slf4j
public class StubMailSender implements JavaMailSender {

    private final Map<String, Object> javaMailProperties = Maps.newHashMap();

    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String PROTOCOL = "protocol";

    private static final String DEFAULT_ENCODING = "defaultEncoding";

    public void setHost(String host) {
        javaMailProperties.put(HOST, host);
    }

    public void setPort(Integer port) {
        javaMailProperties.put(PORT, port);
    }

    public void setUsername(String username) {
        javaMailProperties.put(USERNAME, username);
    }

    public void setPassword(String password) {
        javaMailProperties.put(PASSWORD, password);
    }

    public void setProtocol(String protocol) {
        javaMailProperties.put(PROTOCOL, protocol);
    }

    public void setDefaultEncoding(Charset defaultEncoding) {
        javaMailProperties.put(DEFAULT_ENCODING, defaultEncoding);
    }

    private void assertEmailConfiguration() {
        if (
                !javaMailProperties.keySet().containsAll(ImmutableSet.of(
                        HOST,
                        PORT,
                        USERNAME,
                        PASSWORD,
                        PROTOCOL,
                        DEFAULT_ENCODING
                )) || javaMailProperties.values().stream().anyMatch(Objects::isNull)
        ) {
            throw new IllegalStateException("Service Mail Sender mis-configuration");
        }
    }

    @Override
    public MimeMessage createMimeMessage() {
        return new MimeMessage((Session) null);
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return null;
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        assertEmailConfiguration();

        log.info("Sending Email Message");
        log.info("Mail Sender properties: {}", javaMailProperties);
        try {
            log.info("From: {}", (Object[])mimeMessage.getFrom());
            log.info("To: {}", (Object[])mimeMessage.getAllRecipients());
            log.info("Subject: {}", mimeMessage.getSubject());
            log.info("Body: {}", convertContent(mimeMessage.getContent()));
        } catch (MessagingException | IOException e) {
            throw new MailSendException(e.getMessage());
        }
    }

    private Object convertContent(Object content) throws MessagingException, IOException {
        if (isNull(content)) {
            return "<no content>";
        }

        if (MimeMultipart.class.isAssignableFrom(content.getClass())) {
            MimeMultipart mmContent = (MimeMultipart) content;

            return IntStream.range(0, mmContent.getCount()).mapToObj(value -> {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    BodyPart bp = mmContent.getBodyPart(value);

                    String contentType = bp.getContentType();
                    String contentDisposition = bp.getDisposition();

                    if (TEXT_PLAIN_VALUE.equals(contentType) && isNullOrEmpty(contentDisposition)) {
                        bp.writeTo(baos);
                        return baos.toString().replaceAll("------=_Part_.*", "");
                    } else if (!isNullOrEmpty(contentDisposition)) {
                        return "<" + contentDisposition + ": " + bp.getFileName() + ">";
                    } else {
                        return "<" + contentType + ": not showing>";
                    }
                } catch (MessagingException | IOException e) {
                    return "<content cannot be displayed: " + e.getMessage() + ">";
                }
            }).collect(Collectors.joining("\n"));
        }

        return content;
    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {
        // empty
    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        // empty
    }

    @Override
    public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
        // empty
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        // empty
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        // empty
    }
}

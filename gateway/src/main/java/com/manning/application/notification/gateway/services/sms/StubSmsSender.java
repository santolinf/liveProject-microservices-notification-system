package com.manning.application.notification.gateway.services.sms;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class StubSmsSender {

    @Builder(buildMethodName = "create")
    @Data
    public static class Message {

        public static Message.MessageBuilder creator(
                String toPhoneNumber,
                String fromPhoneNumber,
                String body
        ) {
            return Message.builder()
                    .toPhoneNumber(toPhoneNumber)
                    .fromPhoneNumber(fromPhoneNumber)
                    .body(body);
        }

        private final String toPhoneNumber;
        private final String fromPhoneNumber;
        private final String body;

        private String sid;
    }

    private final Map<String, Object> smsProperties = Maps.newHashMap();

    private static final String ACCOUNT_SID = "accountSID";

    private static final String AUTH_TOKEN = "authToken";

    private static final String FROM_PHONE_NUMBER = "fromPhoneNumber";

    public void setAccountSid(String accountSid) {
        smsProperties.put(ACCOUNT_SID, accountSid);
    }

    public void setAuthToken(String authToken) {
        smsProperties.put(AUTH_TOKEN, authToken);
    }

    public void setFromPhoneNumber(String fromPhoneNumber) {
        smsProperties.put(FROM_PHONE_NUMBER, fromPhoneNumber);
    }

    private void assertSmsConfiguration() {
        if (
                !smsProperties.keySet().containsAll(ImmutableSet.of(
                        ACCOUNT_SID,
                        AUTH_TOKEN
                )) || smsProperties.values().stream().anyMatch(Objects::isNull)
        ) {
            throw new IllegalStateException("Service SMS Sender mis-configuration");
        }
    }

    public void init() {
        log.info("Initialising '{}'", this.getClass().getSimpleName());
        assertSmsConfiguration();
    }

    public void send(Message message) {
        log.info("Sending SMS Message");
        log.info("Sms Sender properties: {}", smsProperties);
        log.info("From phone number: {}", message.getFromPhoneNumber());
        log.info("To phone number: {}", message.getToPhoneNumber());
        log.info("Body: {}", message.getBody());

        message.setSid("SM" + UUID.randomUUID().toString().replace("-", "").toUpperCase());
    }
}

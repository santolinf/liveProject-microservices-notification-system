package com.manning.application.notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.profiles.active=test",
        // disable Consul during testing
        "spring.cloud.consul.enabled=false",
        // disable spring cloud config source loading during testing
        "spring.cloud.consul.config.enabled=false",
        // disable broadcast of configuration changes during testing
        "spring.cloud.bus.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:notificationstestdb"
})
public class NotificationApplicationTests {

    @Test
    void contextLoads() {
    }
}

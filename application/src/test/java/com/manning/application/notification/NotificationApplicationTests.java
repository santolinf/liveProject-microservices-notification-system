package com.manning.application.notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:notificationstestdb"
})
public class NotificationApplicationTests {

    @Test
    void contextLoads() {
    }
}

package com.manning.application.notification.entities;

import com.manning.application.notification.common.model.NotificationMode;
import com.manning.application.notification.common.model.TemplateName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "NOTIFICATIONS")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;

    @Enumerated(EnumType.STRING)
    private NotificationMode notificationMode;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notificationId")
    private List<Parameter> notificationParameters;

    @Enumerated(EnumType.STRING)
    private TemplateName notificationTemplateName;
}

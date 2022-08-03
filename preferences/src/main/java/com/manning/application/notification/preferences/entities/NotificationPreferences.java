package com.manning.application.notification.preferences.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "notification_preferences")
public class NotificationPreferences {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String customerId;

    private Boolean smsPreferenceFlag;

    private Boolean emailPreferenceFlag;

    private String phoneNumber;

    private String emailAddress;
}

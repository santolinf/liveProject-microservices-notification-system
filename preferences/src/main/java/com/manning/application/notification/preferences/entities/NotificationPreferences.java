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

    @Column(name = "customerId")
    private String customerId;

    @Column(name = "smsPreferenceFlag")
    private Boolean smsPreferenceFlag;

    @Column(name = "emailPreferenceFlag")
    private Boolean emailPreferenceFlag;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "emailAddress")
    private String emailAddress;
}

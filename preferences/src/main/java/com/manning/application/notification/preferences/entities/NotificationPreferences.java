package com.manning.application.notification.preferences.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "PREFERENCES")
public class NotificationPreferences {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String customerId;

    private Boolean smsPreferenceFlag;

    private Boolean emailPreferenceFlag;

    private String phoneNumber;

    private String emailAddress;
}

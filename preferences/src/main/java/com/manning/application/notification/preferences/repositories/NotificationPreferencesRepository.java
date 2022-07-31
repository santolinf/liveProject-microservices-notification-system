package com.manning.application.notification.preferences.repositories;

import com.manning.application.notification.preferences.entities.NotificationPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationPreferencesRepository extends JpaRepository<NotificationPreferences, Long> {

    Optional<NotificationPreferences> findByCustomerId(String customerId);
}

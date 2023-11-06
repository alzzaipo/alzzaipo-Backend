package com.alzzaipo.hexagonal.notification.adapter.out.persistence.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailNotificationPersistenceAdapter {

    private final NewEmailNotificationRepository emailNotificationRepository;
}

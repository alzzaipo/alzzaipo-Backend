package com.alzzaipo.hexagonal.notification.application.port.out.email;

import com.alzzaipo.hexagonal.notification.domain.email.EmailNotification;

public interface RegisterEmailNotificationPort {

    void registerEmailNotification(EmailNotification emailNotification);
}

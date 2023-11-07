package com.alzzaipo.hexagonal.notification.application.port.out.email;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.notification.domain.email.EmailNotification;

import java.util.Optional;

public interface FindEmailNotificationPort {

    Optional<EmailNotification> findEmailNotification(Uid memberUID);
}

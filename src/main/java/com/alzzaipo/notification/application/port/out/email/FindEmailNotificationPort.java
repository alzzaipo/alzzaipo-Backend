package com.alzzaipo.notification.application.port.out.email;

import com.alzzaipo.common.Uid;
import com.alzzaipo.notification.domain.email.EmailNotification;

import java.util.Optional;

public interface FindEmailNotificationPort {

    Optional<EmailNotification> findEmailNotification(Uid memberUID);
}

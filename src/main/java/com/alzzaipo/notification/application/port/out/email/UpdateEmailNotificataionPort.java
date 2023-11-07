package com.alzzaipo.notification.application.port.out.email;

import com.alzzaipo.notification.domain.email.EmailNotification;

public interface UpdateEmailNotificataionPort {

    void updateEmailNotificataion(EmailNotification emailNotification);
}

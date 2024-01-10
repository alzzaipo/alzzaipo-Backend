package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.common.email.domain.Email;

public interface SendNotificationEmailVerificationCodeUseCase {

    void sendVerificationCode(Email email);
}

package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.notification.application.port.dto.CheckNotificationEmailVerificationCodeCommand;

public interface CheckNotificationEmailVerificationCodeQuery {

    boolean checkVerificationCode(CheckNotificationEmailVerificationCodeCommand command);
}

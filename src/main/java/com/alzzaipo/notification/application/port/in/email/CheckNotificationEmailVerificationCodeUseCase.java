package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.common.email.domain.EmailVerificationCode;

public interface CheckNotificationEmailVerificationCodeUseCase {

	boolean checkVerificationCode(EmailVerificationCode emailVerificationCode);
}

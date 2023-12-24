package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Uid;

public interface SendNotificationEmailVerificationCodeUseCase {

	void sendVerificationCode(Uid memberId, Email email);
}

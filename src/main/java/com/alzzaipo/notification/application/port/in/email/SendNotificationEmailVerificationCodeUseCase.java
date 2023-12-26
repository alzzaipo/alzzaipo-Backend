package com.alzzaipo.notification.application.port.in.email;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Id;

public interface SendNotificationEmailVerificationCodeUseCase {

	void sendVerificationCode(Id memberId, Email email);
}

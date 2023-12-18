package com.alzzaipo.notification.application.service.email;

import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.verification.VerifyEmailVerificationCodePort;
import com.alzzaipo.notification.application.port.in.email.VerifyNotificationEmailVerificationCodeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyNotificationNotificationEmailVerificationCodeService implements
	VerifyNotificationEmailVerificationCodeUseCase {

	private final VerifyEmailVerificationCodePort verifyEmailVerificationCodePort;

	@Override
	public boolean verify(EmailVerificationCode emailVerificationCode) {
		return verifyEmailVerificationCodePort.verify(emailVerificationCode.get(),
			EmailVerificationPurpose.NOTIFICATION);
	}
}

package com.alzzaipo.notification.application.service.email;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.verification.SaveEmailVerificationCodePort;
import com.alzzaipo.common.email.port.out.smtp.SendEmailVerificationCodePort;
import com.alzzaipo.notification.application.port.in.email.SendNotificationEmailVerificationCodeUseCase;
import com.alzzaipo.notification.application.port.out.email.CheckNotificationEmailAvailablePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendNotificationEmailVerificationCodeService implements SendNotificationEmailVerificationCodeUseCase {

	private final CheckNotificationEmailAvailablePort checkNotificationEmailAvailablePort;
	private final SendEmailVerificationCodePort sendEmailVerificationCodePort;
	private final SaveEmailVerificationCodePort saveEmailVerificationCodePort;

	@Override
	public void send(Uid memberId, Email email) {
		if (checkNotificationEmailAvailablePort.checkEmailAvailable(email.get())) {
			throw new CustomException(HttpStatus.CONFLICT, "이미 등록된 이메일");
		}
		String sentVerificationCode = sendEmailVerificationCodePort.sendVerificationCode(email.get());
		saveEmailVerificationCodePort.save(email.get(), sentVerificationCode,
			memberId.toString(), EmailVerificationPurpose.NOTIFICATION);
	}
}

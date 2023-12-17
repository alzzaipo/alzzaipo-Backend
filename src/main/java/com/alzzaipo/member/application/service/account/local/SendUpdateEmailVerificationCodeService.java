package com.alzzaipo.member.application.service.account.local;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.smtp.SendEmailVerificationCodePort;
import com.alzzaipo.common.email.port.out.verification.SaveEmailVerificationCodePort;
import com.alzzaipo.member.application.port.in.account.local.SendUpdateEmailVerificationCodeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendUpdateEmailVerificationCodeService implements SendUpdateEmailVerificationCodeUseCase {

	private final SendEmailVerificationCodePort sendEmailVerificationCodePort;
	private final SaveEmailVerificationCodePort saveEmailVerificationCodePort;

	@Override
	public void send(@Valid Email email, Uid memberId) {
		String verificationCode = sendEmailVerificationCodePort.sendVerificationCode(email.get());
		saveEmailVerificationCodePort.save(email.get(), verificationCode, memberId.toString(),
			EmailVerificationPurpose.UPDATE);
	}
}

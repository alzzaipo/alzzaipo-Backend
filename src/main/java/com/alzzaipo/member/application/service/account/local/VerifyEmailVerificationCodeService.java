package com.alzzaipo.member.application.service.account.local;

import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.verification.VerifyEmailVerificationCodePort;
import com.alzzaipo.member.application.port.in.account.local.VerifyEmailVerificationCodeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerifyEmailVerificationCodeService implements VerifyEmailVerificationCodeUseCase {

	private final VerifyEmailVerificationCodePort verifyEmailVerificationCodePort;

	@Override
	public boolean verify(@Valid EmailVerificationCode verificationCode, EmailVerificationPurpose purpose) {
		return verifyEmailVerificationCodePort.verify(verificationCode.get(), purpose);
	}
}

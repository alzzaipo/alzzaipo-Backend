package com.alzzaipo.member.application.service.account.local;

import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.smtp.SendEmailVerificationCodePort;
import com.alzzaipo.common.email.port.out.verification.SaveEmailVerificationCodePort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountIdAvailabilityQuery;
import com.alzzaipo.member.application.port.in.account.local.SendSignUpEmailVerificationCodeUseCase;
import com.alzzaipo.member.application.port.in.dto.SendSignUpEmailVerificationCodeCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSignUpEmailVerificationCodeService implements SendSignUpEmailVerificationCodeUseCase {

	private final CheckLocalAccountIdAvailabilityQuery checkLocalAccountIdAvailabilityQuery;
	private final SendEmailVerificationCodePort sendEmailVerificationCodePort;
	private final SaveEmailVerificationCodePort saveEmailVerificationCodePort;

	@Override
	public void send(@Valid SendSignUpEmailVerificationCodeCommand command) {
		if (!checkLocalAccountIdAvailabilityQuery.check(command.getAccountId())) {
			throw new CustomException(HttpStatus.CONFLICT, "중복된 아이디");
		}
		String sentVerificationCode = sendEmailVerificationCodePort.sendVerificationCode(command.getEmail().get());
		saveEmailVerificationCodePort.save(command.getEmail().get(), sentVerificationCode,
			command.getAccountId().get(), EmailVerificationPurpose.SIGN_UP);
	}
}

package com.alzzaipo.member.application.service.account.local;

import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.verification.CheckEmailVerifiedPort;
import com.alzzaipo.common.email.port.out.verification.DeleteEmailVerificationStatusPort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountEmailAvailabilityQuery;
import com.alzzaipo.member.application.port.in.account.local.CheckLocalAccountIdAvailabilityQuery;
import com.alzzaipo.member.application.port.in.account.local.RegisterLocalAccountUseCase;
import com.alzzaipo.member.application.port.in.dto.RegisterLocalAccountCommand;
import com.alzzaipo.member.application.port.out.account.local.RegisterLocalAccountPort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.application.port.out.member.RegisterMemberPort;
import com.alzzaipo.member.domain.account.local.LocalAccountId;
import com.alzzaipo.member.domain.member.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterLocalAccountService implements RegisterLocalAccountUseCase {

	private final static EmailVerificationPurpose EMAIL_VERIFICATION_PURPOSE = EmailVerificationPurpose.SIGN_UP;

	private final PasswordEncoder passwordEncoder;
	private final CheckLocalAccountIdAvailabilityQuery checkLocalAccountIdAvailabilityQuery;
	private final CheckLocalAccountEmailAvailabilityQuery checkLocalAccountEmailAvailabilityQuery;
	private final CheckEmailVerifiedPort checkEmailVerifiedPort;
	private final RegisterMemberPort registerMemberPort;
	private final RegisterLocalAccountPort registerLocalAccountPort;
	private final DeleteEmailVerificationStatusPort deleteEmailVerificationStatusPort;

	@Override
	public void registerLocalAccount(@Valid RegisterLocalAccountCommand command) {
		String plainLocalAccountPassword = command.getLocalAccountPassword().get();
		String encryptedLocalAccountPassword = passwordEncoder.encode(plainLocalAccountPassword);

		checkAccountIdAvailability(command.getLocalAccountId());
		checkEmailAvailability(command.getEmail());
		checkEmailVerified(command.getEmail(), command.getLocalAccountId());

		Member member = Member.create(command.getNickname());

		SecureLocalAccount secureLocalAccount = new SecureLocalAccount(member.getUid(),
			command.getLocalAccountId(),
			encryptedLocalAccountPassword,
			command.getEmail());

		registerMemberPort.registerMember(member);
		registerLocalAccountPort.registerLocalAccountPort(secureLocalAccount);
		deleteEmailVerificationStatusPort.delete(command.getEmail().get(), EMAIL_VERIFICATION_PURPOSE);
	}

	private void checkAccountIdAvailability(LocalAccountId localAccountId) {
		if (!checkLocalAccountIdAvailabilityQuery.check(localAccountId)) {
			throw new CustomException(HttpStatus.CONFLICT, "아이디 중복");
		}
	}

	private void checkEmailAvailability(Email email) {
		if (!checkLocalAccountEmailAvailabilityQuery.check(email)) {
			throw new CustomException(HttpStatus.CONFLICT, "이메일 중복");
		}
	}

	private void checkEmailVerified(Email email, LocalAccountId localAccountId) {
		if (!checkEmailVerifiedPort.check(email.get(), localAccountId.get(), EMAIL_VERIFICATION_PURPOSE)) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "인증되지 않은 이메일");
		}
	}
}

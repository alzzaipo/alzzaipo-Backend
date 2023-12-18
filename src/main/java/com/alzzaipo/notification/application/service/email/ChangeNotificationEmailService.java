package com.alzzaipo.notification.application.service.email;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.verification.CheckEmailVerifiedPort;
import com.alzzaipo.common.email.port.out.verification.DeleteEmailVerificationStatusPort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.member.FindMemberAccountEmailsQuery;
import com.alzzaipo.notification.application.port.in.email.ChangeNotificationEmailUseCase;
import com.alzzaipo.notification.application.port.out.email.ChangeNotificationEmailPort;
import com.alzzaipo.notification.application.port.out.email.CheckMemberSubscriptionExists;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeNotificationEmailService implements ChangeNotificationEmailUseCase {

	private final static EmailVerificationPurpose EMAIL_VERIFICATION_PURPOSE = EmailVerificationPurpose.NOTIFICATION;

	private final CheckMemberSubscriptionExists checkMemberSubscriptionExists;
	private final FindMemberAccountEmailsQuery findMemberAccountEmailsQuery;
	private final CheckEmailVerifiedPort checkEmailVerifiedPort;
	private final ChangeNotificationEmailPort changeNotificationEmailPort;
	private final DeleteEmailVerificationStatusPort deleteEmailVerificationStatusPort;

	@Override
	public void changeEmail(Uid memberId, @Valid Email email) {
		validate(memberId, email);
		changeNotificationEmailPort.changeEmail(memberId.get(), email.get());
		deleteEmailVerificationStatusPort.delete(email.get(), EMAIL_VERIFICATION_PURPOSE);
	}

	private void validate(Uid memberId, Email email) {
		if (!checkMemberSubscriptionExists.checkSubscription(memberId)) {
			throw new CustomException(HttpStatus.FORBIDDEN, "구독 내역 없음");
		}
		if (!checkEmailVerified(memberId, email)) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "이메일 미인증");
		}
	}

	private boolean checkEmailVerified(Uid memberId, @Valid Email newEmail) {
		boolean matched = findMemberAccountEmailsQuery.findEmails(memberId).stream()
			.anyMatch(accountEmail -> accountEmail.equals(newEmail.get()));

		return matched || checkEmailVerifiedPort.check(newEmail.get(), memberId.toString(), EMAIL_VERIFICATION_PURPOSE);
	}
}

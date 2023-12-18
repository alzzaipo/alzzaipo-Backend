package com.alzzaipo.notification.application.service.email;

import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.verification.CheckEmailVerifiedPort;
import com.alzzaipo.common.email.port.out.verification.DeleteEmailVerificationStatusPort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.member.FindMemberAccountEmailsQuery;
import com.alzzaipo.notification.application.port.dto.SubscribeEmailNotificationCommand;
import com.alzzaipo.notification.application.port.in.email.SubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.out.email.CheckMemberSubscriptionExists;
import com.alzzaipo.notification.application.port.out.email.RegisterEmailNotificationPort;
import com.alzzaipo.notification.domain.email.EmailNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeEmailNotificationService implements SubscribeEmailNotificationUseCase {

	private final static EmailVerificationPurpose EMAIL_VERIFICATION_PURPOSE = EmailVerificationPurpose.NOTIFICATION;

	private final CheckMemberSubscriptionExists checkMemberSubscriptionExists;
	private final FindMemberAccountEmailsQuery findMemberAccountEmailsQuery;
	private final CheckEmailVerifiedPort checkEmailVerifiedPort;
	private final RegisterEmailNotificationPort registerEmailNotificationPort;
	private final DeleteEmailVerificationStatusPort deleteEmailVerificationStatusPort;

	@Override
	public void subscribe(SubscribeEmailNotificationCommand command) {
		if (checkMemberSubscriptionExists.checkSubscription(command.getMemberUID())) {
			throw new CustomException(HttpStatus.CONFLICT, "구독된 상태입니다.");
		}
		if (!checkEmailVerified(command)) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "이메일 미인증");
		}

		EmailNotification emailNotification = new EmailNotification(command.getMemberUID(), command.getEmail());
		registerEmailNotificationPort.register(emailNotification);
		deleteEmailVerificationStatusPort.delete(command.getEmail().get(), EMAIL_VERIFICATION_PURPOSE);
	}

	private boolean checkEmailVerified(SubscribeEmailNotificationCommand command) {
		boolean matched = findMemberAccountEmailsQuery.findEmails(command.getMemberUID())
			.stream()
			.anyMatch(accountEmail -> accountEmail.equals(command.getEmail().get()));

		return matched || checkEmailVerifiedPort.check(command.getEmail().get(),
			command.getMemberUID().toString(),
			EMAIL_VERIFICATION_PURPOSE);
	}
}

package com.alzzaipo.notification.application.service;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.port.out.smtp.SendEmailVerificationCodePort;
import com.alzzaipo.common.email.port.out.verification.CheckEmailVerifiedPort;
import com.alzzaipo.common.email.port.out.verification.DeleteEmailVerificationStatusPort;
import com.alzzaipo.common.email.port.out.verification.SaveEmailVerificationCodePort;
import com.alzzaipo.common.email.port.out.verification.VerifyEmailVerificationCodePort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.member.FindMemberAccountEmailsQuery;
import com.alzzaipo.notification.application.port.dto.DeleteNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.dto.EmailNotificationStatus;
import com.alzzaipo.notification.application.port.dto.NotificationCriterionView;
import com.alzzaipo.notification.application.port.dto.RegisterNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.dto.SubscribeEmailNotificationCommand;
import com.alzzaipo.notification.application.port.dto.UpdateNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.in.criterion.DeleteNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.in.criterion.FindMemberNotificationCriteriaQuery;
import com.alzzaipo.notification.application.port.in.criterion.RegisterNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.in.criterion.UpdateNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.in.email.ChangeNotificationEmailUseCase;
import com.alzzaipo.notification.application.port.in.email.FindEmailNotificationStatusQuery;
import com.alzzaipo.notification.application.port.in.email.SendNotificationEmailVerificationCodeUseCase;
import com.alzzaipo.notification.application.port.in.email.SubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.in.email.UnsubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.in.email.CheckNotificationEmailVerificationCodeUseCase;
import com.alzzaipo.notification.application.port.out.criterion.CheckNotificationCriterionOwnershipPort;
import com.alzzaipo.notification.application.port.out.criterion.CountMemberNotificationCriteriaPort;
import com.alzzaipo.notification.application.port.out.criterion.DeleteNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.criterion.FindMemberNotificationCriteriaPort;
import com.alzzaipo.notification.application.port.out.criterion.RegisterNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.criterion.UpdateNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.email.ChangeNotificationEmailPort;
import com.alzzaipo.notification.application.port.out.email.CheckMemberSubscriptionExistsPort;
import com.alzzaipo.notification.application.port.out.email.CheckNotificationEmailAvailablePort;
import com.alzzaipo.notification.application.port.out.email.DeleteEmailNotificationPort;
import com.alzzaipo.notification.application.port.out.email.FindNotificationEmailPort;
import com.alzzaipo.notification.application.port.out.email.RegisterEmailNotificationPort;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import com.alzzaipo.notification.domain.email.EmailNotification;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService implements RegisterNotificationCriterionUseCase,
	FindMemberNotificationCriteriaQuery,
	UpdateNotificationCriterionUseCase,
	DeleteNotificationCriterionUseCase,
	FindEmailNotificationStatusQuery,
	SendNotificationEmailVerificationCodeUseCase,
	CheckNotificationEmailVerificationCodeUseCase,
	SubscribeEmailNotificationUseCase,
	UnsubscribeEmailNotificationUseCase,
	ChangeNotificationEmailUseCase {

	@Value("${NOTIFICATION_CRITERIA_LIMIT}")
	private int NOTIFICATION_CRITERIA_LIMIT;

	private static final EmailVerificationPurpose EMAIL_VERIFICATION_PURPOSE = EmailVerificationPurpose.NOTIFICATION;

	private final RegisterNotificationCriterionPort registerNotificationCriterionPort;
	private final FindMemberNotificationCriteriaPort findMemberNotificationCriteriaPort;
	private final UpdateNotificationCriterionPort updateNotificationCriterionPort;
	private final DeleteNotificationCriterionPort deleteNotificationCriterionPort;
	private final CountMemberNotificationCriteriaPort countMemberNotificationCriteriaPort;
	private final CheckNotificationCriterionOwnershipPort checkNotificationCriterionOwnershipPort;
	private final FindNotificationEmailPort findNotificationEmailPort;
	private final CheckNotificationEmailAvailablePort checkNotificationEmailAvailablePort;
	private final SendEmailVerificationCodePort sendEmailVerificationCodePort;
	private final SaveEmailVerificationCodePort saveEmailVerificationCodePort;
	private final VerifyEmailVerificationCodePort verifyEmailVerificationCodePort;
	private final CheckMemberSubscriptionExistsPort checkMemberSubscriptionExistsPort;
	private final FindMemberAccountEmailsQuery findMemberAccountEmailsQuery;
	private final CheckEmailVerifiedPort checkEmailVerifiedPort;
	private final RegisterEmailNotificationPort registerEmailNotificationPort;
	private final DeleteEmailVerificationStatusPort deleteEmailVerificationStatusPort;
	private final DeleteEmailNotificationPort deleteEmailNotificationPort;
	private final ChangeNotificationEmailPort changeNotificationEmailPort;

	@Override
	public void registerNotificationCriterion(RegisterNotificationCriterionCommand command) {
		NotificationCriterion notificationCriterion = NotificationCriterion.build(command.getMemberUID(),
			command.getMinCompetitionRate(),
			command.getMinLockupRate());

		checkNotificationCriteriaCapacity(command.getMemberUID());

		registerNotificationCriterionPort.registerNotificationCriterion(notificationCriterion);
	}

	@Override
	@Transactional(readOnly = true)
	public List<NotificationCriterionView> findMemberNotificationCriteria(Uid memberUID) {
		return findMemberNotificationCriteriaPort.findMemberNotificationCriteria(memberUID)
			.stream()
			.map(NotificationCriterionView::build)
			.collect(Collectors.toList());
	}

	@Override
	public void updateNotificationCriterion(UpdateNotificationCriterionCommand command) {
		checkNotificationCriterionOwnership(command.getMemberUID(), command.getNotificationCriterionUID());
		updateNotificationCriterionPort.updateNotificationCriterion(command);
	}

	@Override
	public void deleteNotificationCriterion(DeleteNotificationCriterionCommand command) {
		checkNotificationCriterionOwnership(command.getMemberUID(), command.getNotificationCriterionUID());
		deleteNotificationCriterionPort.deleteNotificationCriterion(command.getNotificationCriterionUID());
	}

	@Override
	@Transactional(readOnly = true)
	public EmailNotificationStatus findStatus(Uid memberUID) {
		Optional<String> notificationEmail = findNotificationEmailPort.findNotificationEmail(memberUID);
		return new EmailNotificationStatus(notificationEmail.isPresent(), notificationEmail.orElse(""));
	}

	@Override
	public void sendVerificationCode(Uid memberId, Email email) {
		if (checkNotificationEmailAvailablePort.checkEmailAvailable(email.get())) {
			throw new CustomException(HttpStatus.CONFLICT, "이미 등록된 이메일");
		}
		String sentVerificationCode = sendEmailVerificationCodePort.sendVerificationCode(email.get());
		saveEmailVerificationCodePort.save(email.get(), sentVerificationCode, memberId.toString(), EMAIL_VERIFICATION_PURPOSE);
	}

	@Override
	public boolean checkVerificationCode(EmailVerificationCode emailVerificationCode) {
		return verifyEmailVerificationCodePort.verify(emailVerificationCode.get(), EMAIL_VERIFICATION_PURPOSE);
	}

	@Override
	public void subscribeEmailNotification(SubscribeEmailNotificationCommand command) {
		if (checkMemberSubscriptionExistsPort.checkSubscription(command.getMemberUID())) {
			throw new CustomException(HttpStatus.CONFLICT, "구독 내역 존재");
		}
		checkEmailVerified(command.getMemberUID(), command.getEmail());

		EmailNotification emailNotification = new EmailNotification(command.getMemberUID(), command.getEmail());
		registerEmailNotificationPort.register(emailNotification);
		deleteEmailVerificationStatusPort.delete(command.getEmail().get(), EMAIL_VERIFICATION_PURPOSE);
	}

	@Override
	public void unsubscribeEmailNotification(Uid memberUID) {
		deleteEmailNotificationPort.delete(memberUID);
	}

	@Override
	public void changeNotificationEmail(Uid memberId, @Valid Email email) {
		if (!checkMemberSubscriptionExistsPort.checkSubscription(memberId)) {
			throw new CustomException(HttpStatus.FORBIDDEN, "구독 내역 없음");
		}
		checkEmailVerified(memberId, email);

		changeNotificationEmailPort.changeEmail(memberId.get(), email.get());
		deleteEmailVerificationStatusPort.delete(email.get(), EMAIL_VERIFICATION_PURPOSE);
	}

	private void checkNotificationCriteriaCapacity(Uid memberID) {
		int totalCount = countMemberNotificationCriteriaPort.count(memberID.get());
		if (totalCount >= NOTIFICATION_CRITERIA_LIMIT) {
			throw new CustomException(HttpStatus.FORBIDDEN, "오류 : 최대 개수 초과");
		}
	}

	private void checkNotificationCriterionOwnership(Uid memberId, Uid notificationCriterionId) {
		if (!checkNotificationCriterionOwnershipPort.checkOwnership(memberId.get(), notificationCriterionId.get())) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "오류: 권한 없음");
		}
	}

	private void checkEmailVerified(Uid memberId, Email email) {
		boolean isAccountEmail = findMemberAccountEmailsQuery.findEmails(memberId)
			.stream()
			.anyMatch(accountEmail -> accountEmail.equals(email.get()));

		boolean isVerified = checkEmailVerifiedPort.check(email.get(),
			memberId.toString(),
			EMAIL_VERIFICATION_PURPOSE);

		if (!isAccountEmail && !isVerified) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "이메일 미인증");
		}
	}
}

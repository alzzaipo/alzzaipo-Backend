package com.alzzaipo.notification.application.service;

import com.alzzaipo.common.Id;
import com.alzzaipo.common.email.domain.Email;
import com.alzzaipo.common.email.domain.EmailVerificationCode;
import com.alzzaipo.common.email.domain.EmailVerificationPurpose;
import com.alzzaipo.common.email.application.port.out.smtp.SendEmailVerificationCodePort;
import com.alzzaipo.common.email.application.port.out.verification.CheckEmailVerificationCodePort;
import com.alzzaipo.common.email.application.port.out.verification.DeleteEmailVerificationStatusPort;
import com.alzzaipo.common.email.application.port.out.verification.SaveEmailVerificationCodePort;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.in.member.FindMemberAccountEmailsQuery;
import com.alzzaipo.notification.application.port.dto.ChangeNotificationEmailCommand;
import com.alzzaipo.notification.application.port.dto.CheckNotificationEmailVerificationCodeCommand;
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
import com.alzzaipo.notification.application.port.in.email.CheckNotificationEmailVerificationCodeQuery;
import com.alzzaipo.notification.application.port.in.email.FindEmailNotificationStatusQuery;
import com.alzzaipo.notification.application.port.in.email.SendNotificationEmailVerificationCodeUseCase;
import com.alzzaipo.notification.application.port.in.email.SubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.in.email.UnsubscribeEmailNotificationUseCase;
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
    CheckNotificationEmailVerificationCodeQuery,
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
    private final CheckMemberSubscriptionExistsPort checkMemberSubscriptionExistsPort;
    private final FindMemberAccountEmailsQuery findMemberAccountEmailsQuery;
    private final CheckEmailVerificationCodePort checkEmailVerificationCodePort;
    private final RegisterEmailNotificationPort registerEmailNotificationPort;
    private final DeleteEmailVerificationStatusPort deleteEmailVerificationStatusPort;
    private final DeleteEmailNotificationPort deleteEmailNotificationPort;
    private final ChangeNotificationEmailPort changeNotificationEmailPort;

    @Override
    public void registerNotificationCriterion(RegisterNotificationCriterionCommand command) {
        NotificationCriterion notificationCriterion = NotificationCriterion.build(command.getMemberId(),
            command.getMinCompetitionRate(),
            command.getMinLockupRate());

        checkNotificationCriteriaCapacity(command.getMemberId());

        registerNotificationCriterionPort.registerNotificationCriterion(notificationCriterion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationCriterionView> findMemberNotificationCriteria(Id memberId) {
        return findMemberNotificationCriteriaPort.findMemberNotificationCriteria(memberId)
            .stream()
            .map(NotificationCriterionView::build)
            .collect(Collectors.toList());
    }

    @Override
    public void updateNotificationCriterion(UpdateNotificationCriterionCommand command) {
        checkNotificationCriterionOwnership(command.getMemberId(), command.getNotificationCriterionId());
        updateNotificationCriterionPort.updateNotificationCriterion(command);
    }

    @Override
    public void deleteNotificationCriterion(DeleteNotificationCriterionCommand command) {
        checkNotificationCriterionOwnership(command.getMemberId(), command.getNotificationCriterionId());
        deleteNotificationCriterionPort.deleteNotificationCriterion(command.getNotificationCriterionId());
    }

    @Override
    @Transactional(readOnly = true)
    public EmailNotificationStatus findStatus(Id memberId) {
        Optional<String> notificationEmail = findNotificationEmailPort.findNotificationEmail(memberId);
        return new EmailNotificationStatus(notificationEmail.isPresent(), notificationEmail.orElse(""));
    }

    @Override
    public void sendVerificationCode(Email email) {
        if (checkNotificationEmailAvailablePort.checkEmailAvailable(email)) {
            throw new CustomException(HttpStatus.CONFLICT, "이미 등록된 이메일");
        }
        EmailVerificationCode sentVerificationCode = sendEmailVerificationCodePort.sendVerificationCode(email);
        saveEmailVerificationCodePort.save(email, sentVerificationCode, EMAIL_VERIFICATION_PURPOSE);
    }

    @Override
    public boolean checkVerificationCode(CheckNotificationEmailVerificationCodeCommand command) {
        return checkEmailVerificationCodePort.check(
            command.getEmail(),
            command.getEmailVerificationCode(),
            EMAIL_VERIFICATION_PURPOSE);
    }

    @Override
    public void subscribeEmailNotification(SubscribeEmailNotificationCommand command) {
        if (checkMemberSubscriptionExistsPort.checkSubscription(command.getMemberId())) {
            throw new CustomException(HttpStatus.CONFLICT, "구독 내역 존재");
        }
        checkEmailVerified(command.getMemberId(), command.getEmail(), command.getEmailVerificationCode());

        EmailNotification emailNotification = new EmailNotification(command.getMemberId(), command.getEmail());

        registerEmailNotificationPort.register(emailNotification);

        deleteEmailVerificationStatusPort.delete(command.getEmail(), EMAIL_VERIFICATION_PURPOSE);
    }

    @Override
    public void unsubscribeEmailNotification(Id memberId) {
        deleteEmailNotificationPort.delete(memberId);
    }

    @Override
    public void changeNotificationEmail(ChangeNotificationEmailCommand command) {
        if (!checkMemberSubscriptionExistsPort.checkSubscription(command.getMemberId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "구독 내역 없음");
        }
        checkEmailVerified(command.getMemberId(), command.getEmail(), command.getEmailVerificationCode());

        changeNotificationEmailPort.changeEmail(command.getMemberId(), command.getEmail());
        deleteEmailVerificationStatusPort.delete(command.getEmail(), EMAIL_VERIFICATION_PURPOSE);
    }

    private void checkNotificationCriteriaCapacity(Id memberId) {
        int totalCount = countMemberNotificationCriteriaPort.count(memberId);
        if (totalCount >= NOTIFICATION_CRITERIA_LIMIT) {
            throw new CustomException(HttpStatus.FORBIDDEN, "오류 : 최대 개수 초과");
        }
    }

    private void checkNotificationCriterionOwnership(Id memberId, Id notificationCriterionId) {
        if (!checkNotificationCriterionOwnershipPort.checkOwnership(memberId, notificationCriterionId)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "오류: 권한 없음");
        }
    }

    private void checkEmailVerified(Id memberId, Email email, EmailVerificationCode emailVerificationCode) {
        boolean isAccountEmail = findMemberAccountEmailsQuery.findEmails(memberId)
            .stream()
            .anyMatch(accountEmail -> accountEmail.equals(email.get()));

        boolean isVerificationCodeValid = checkEmailVerificationCodePort.check(email, emailVerificationCode,
            EMAIL_VERIFICATION_PURPOSE);

        if (!isAccountEmail && !isVerificationCodeValid) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "이메일 미인증");
        }
    }
}

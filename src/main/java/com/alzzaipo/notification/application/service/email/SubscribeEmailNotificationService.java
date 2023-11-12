package com.alzzaipo.notification.application.service.email;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.email.application.port.out.CheckEmailVerifiedPort;
import com.alzzaipo.email.application.port.out.DeleteOldEmailVerificationHistoryPort;
import com.alzzaipo.member.application.port.out.account.local.FindLocalAccountByMemberUidPort;
import com.alzzaipo.member.application.port.out.account.social.FindSocialAccountByLoginTypePort;
import com.alzzaipo.member.application.port.out.dto.SecureLocalAccount;
import com.alzzaipo.member.domain.account.social.SocialAccount;
import com.alzzaipo.notification.application.port.dto.SubscribeEmailNotificationCommand;
import com.alzzaipo.notification.application.port.in.email.SubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.out.email.FindEmailNotificationPort;
import com.alzzaipo.notification.application.port.out.email.RegisterEmailNotificationPort;
import com.alzzaipo.notification.domain.email.EmailNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscribeEmailNotificationService implements SubscribeEmailNotificationUseCase {

    private final CheckEmailVerifiedPort checkEmailVerifiedPort;
    private final FindEmailNotificationPort findEmailNotificationPort;
    private final RegisterEmailNotificationPort registerEmailNotificationPort;
    private final DeleteOldEmailVerificationHistoryPort deleteOldEmailVerificationHistoryPort;
    private final FindLocalAccountByMemberUidPort findLocalAccountByMemberUidPort;
    private final FindSocialAccountByLoginTypePort findSocialAccountByLoginTypePort;

    @Override
    public void subscribeEmailNotification(SubscribeEmailNotificationCommand command) {
        validate(command);

        EmailNotification emailNotification = new EmailNotification(command.getMemberUID(), command.getEmail());
        registerEmailNotificationPort.registerEmailNotification(emailNotification);

        deleteOldEmailVerificationHistoryPort.deleteOldEmailVerificationHistory(command.getEmail());
    }

    private void validate(SubscribeEmailNotificationCommand command) {
        if (isAlreadyRegistered(command)) {
            throw new CustomException(HttpStatus.CONFLICT, "오류 : 이미 구독된 상태");
        }

        if (!isEmailVerified(command)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "오류 : 미인증 이메일");
        }
    }

    private boolean isAlreadyRegistered(SubscribeEmailNotificationCommand command) {
        return findEmailNotificationPort.findEmailNotification(command.getMemberUID()).isPresent();
    }

    private boolean isEmailVerified(SubscribeEmailNotificationCommand command) {
        Email currentEmail = findCurrentEmail(command);

        if (currentEmail.equals(command.getEmail())) {
            return true;
        }
        return checkEmailVerifiedPort.checkEmailVerified(command.getEmail());
    }

    private Email findCurrentEmail(SubscribeEmailNotificationCommand command) {
        if (command.getLoginType().equals(LoginType.LOCAL)) {
            return findLocalAccountByMemberUidPort.findLocalAccountByMemberUid(command.getMemberUID())
                    .map(SecureLocalAccount::getEmail)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "계정 조회 실패"));
        }
        return findSocialAccountByLoginTypePort.findSocialAccountByLoginType(command.getMemberUID(), command.getLoginType())
                .map(SocialAccount::getEmail)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "계정 조회 실패"));
    }
}

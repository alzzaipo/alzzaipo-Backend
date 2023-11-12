package com.alzzaipo.notification.application.service.email;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.email.application.port.out.CheckEmailVerifiedPort;
import com.alzzaipo.email.application.port.out.DeleteOldEmailVerificationHistoryPort;
import com.alzzaipo.notification.application.port.in.email.SubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.out.email.FindEmailNotificationPort;
import com.alzzaipo.notification.application.port.out.email.RegisterEmailNotificationPort;
import com.alzzaipo.notification.domain.email.EmailNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubscribeEmailNotificationService implements SubscribeEmailNotificationUseCase {

    private final CheckEmailVerifiedPort checkEmailVerifiedPort;
    private final FindEmailNotificationPort findEmailNotificationPort;
    private final RegisterEmailNotificationPort registerEmailNotificationPort;
    private final DeleteOldEmailVerificationHistoryPort deleteOldEmailVerificationHistoryPort;

    @Override
    public void subscribeEmailNotification(Uid memberUID, Email email) {
        validate(memberUID, email);

        EmailNotification emailNotification = new EmailNotification(memberUID, email);
        registerEmailNotificationPort.registerEmailNotification(emailNotification);

        deleteOldEmailVerificationHistoryPort.deleteOldEmailVerificationHistory(email);
    }

    private void validate(Uid memberUID, Email email) {
        Optional<EmailNotification> optionalEmailNotification = findEmailNotificationPort.findEmailNotification(memberUID);

        if (optionalEmailNotification.isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "오류 : 이미 구독된 상태");
        }

        if (!checkEmailVerifiedPort.checkEmailVerified(email)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "오류 : 미인증 이메일");
        }
    }
}

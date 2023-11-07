package com.alzzaipo.hexagonal.notification.application.service.email;

import com.alzzaipo.hexagonal.common.Email;
import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.email.application.port.out.CheckEmailVerifiedPort;
import com.alzzaipo.hexagonal.email.application.port.out.DeleteOldEmailVerificationHistoryPort;
import com.alzzaipo.hexagonal.notification.application.port.in.email.SubscribeEmailNotificationUseCase;
import com.alzzaipo.hexagonal.notification.application.port.out.email.FindEmailNotificationPort;
import com.alzzaipo.hexagonal.notification.application.port.out.email.RegisterEmailNotificationPort;
import com.alzzaipo.hexagonal.notification.domain.email.EmailNotification;
import lombok.RequiredArgsConstructor;
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
            throw new RuntimeException("오류 : 이미 구독된 상태");
        }

        if (!checkEmailVerifiedPort.checkEmailVerified(email)) {
            throw new RuntimeException("오류 : 미인증 이메일");
        }
    }
}

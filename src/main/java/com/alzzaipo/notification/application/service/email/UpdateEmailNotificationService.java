package com.alzzaipo.notification.application.service.email;

import com.alzzaipo.common.Email;
import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.email.application.port.out.CheckEmailVerifiedPort;
import com.alzzaipo.email.application.port.out.DeleteOldEmailVerificationHistoryPort;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.notification.application.port.in.email.UpdateEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.out.email.FindEmailNotificationPort;
import com.alzzaipo.notification.application.port.out.email.UpdateEmailNotificataionPort;
import com.alzzaipo.notification.domain.email.EmailNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateEmailNotificationService implements UpdateEmailNotificationUseCase {

    private final FindMemberPort findMemberPort;
    private final FindEmailNotificationPort findEmailNotificationPort;
    private final CheckEmailVerifiedPort checkEmailVerifiedPort;
    private final UpdateEmailNotificataionPort updateEmailNotificataionPort;
    private final DeleteOldEmailVerificationHistoryPort deleteOldEmailVerificationHistoryPort;

    @Override
    public void updateEmailNotification(Uid memberUID, Email newEmail) {
        validateMemberAndEmail(memberUID, newEmail);

        EmailNotification newEmailNotification = new EmailNotification(memberUID, newEmail);

        updateEmailNotificataionPort.updateEmailNotificataion(newEmailNotification);

        deleteOldEmailVerificationHistoryPort.deleteOldEmailVerificationHistory(newEmail);
    }

    private void validateMemberAndEmail(Uid memberUID, Email newEmail) {
        findMemberPort.findMember(memberUID)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패"));

        findEmailNotificationPort.findEmailNotification(memberUID)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이메일 알림 내역 조회 실패"));

        if (!checkEmailVerifiedPort.checkEmailVerified(newEmail)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "오류 : 이메일 미인증");
        }
    }
}

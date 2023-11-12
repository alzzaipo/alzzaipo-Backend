package com.alzzaipo.notification.application.service.email;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.notification.application.port.in.email.UnsubscribeEmailNotificationUseCase;
import com.alzzaipo.notification.application.port.out.UnsubscribeEmailNotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnsubscribeEmailNotificationService implements UnsubscribeEmailNotificationUseCase {

    private final FindMemberPort findMemberPort;
    private final UnsubscribeEmailNotificationPort unsubscribeEmailNotificationPort;

    @Override
    public void unsubscribeEmailNotification(Uid memberUID) {
        validateMember(memberUID);

        unsubscribeEmailNotificationPort.unsubscribeEmailNotification(memberUID);
    }

    private void validateMember(Uid memberUID) {
        findMemberPort.findMember(memberUID)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패"));
    }
}

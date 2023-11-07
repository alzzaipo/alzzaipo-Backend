package com.alzzaipo.hexagonal.notification.application.service.email;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.hexagonal.notification.application.port.in.email.UnsubscribeEmailNotificationUseCase;
import com.alzzaipo.hexagonal.notification.application.port.out.UnsubscribeEmailNotificationPort;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));
    }
}

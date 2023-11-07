package com.alzzaipo.notification.application.service.email;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.notification.application.port.dto.EmailNotificationStatus;
import com.alzzaipo.notification.application.port.in.email.FindEmailNotificationStatusQuery;
import com.alzzaipo.notification.application.port.out.email.FindEmailNotificationPort;
import com.alzzaipo.notification.domain.email.EmailNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindEmailNotificationService implements FindEmailNotificationStatusQuery {

    private final FindMemberPort findMemberPort;
    private final FindEmailNotificationPort findEmailNotificationPort;

    @Override
    public EmailNotificationStatus findEmailNotificationStatus(Uid memberUID) {
        validateMember(memberUID);

        Optional<EmailNotification> emailNotification = findEmailNotificationPort.findEmailNotification(memberUID);

        boolean subscriptionStatus = emailNotification.isPresent();
        String email = emailNotification.map(e -> e.getEmail().get()).orElse(null);

        return new EmailNotificationStatus(subscriptionStatus, email);
    }

    private void validateMember(Uid memberUID) {
        findMemberPort.findMember(memberUID)
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));
    }

}

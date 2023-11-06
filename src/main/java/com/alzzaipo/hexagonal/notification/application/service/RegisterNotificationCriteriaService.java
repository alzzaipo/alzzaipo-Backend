package com.alzzaipo.hexagonal.notification.application.service;

import com.alzzaipo.hexagonal.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.hexagonal.notification.application.port.dto.RegisterNotificationCriteriaCommand;
import com.alzzaipo.hexagonal.notification.application.port.in.RegisterNotificationCriteriaUseCase;
import com.alzzaipo.hexagonal.notification.application.port.out.RegisterNotificationCriteriaPort;
import com.alzzaipo.hexagonal.notification.domain.NotificationCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterNotificationCriteriaService implements RegisterNotificationCriteriaUseCase {

    private final RegisterNotificationCriteriaPort registerNotificationCriteriaPort;
    private final FindMemberPort findMemberPort;

    @Override
    public void registerNotificationCriteria(RegisterNotificationCriteriaCommand command) {
        findMemberPort.findMember(command.getMemberUID())
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        NotificationCriteria notificationCriteria = NotificationCriteria.create(
                command.getMemberUID(),
                command.getMinCompetitionRate(),
                command.getMinLockupRate());

        registerNotificationCriteriaPort.registerNotificationCriteria(notificationCriteria);
    }
}

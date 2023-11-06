package com.alzzaipo.hexagonal.notification.application.service;

import com.alzzaipo.hexagonal.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.hexagonal.notification.application.port.dto.RegisterNotificationCriterionCommand;
import com.alzzaipo.hexagonal.notification.application.port.in.RegisterNotificationCriterionUseCase;
import com.alzzaipo.hexagonal.notification.application.port.out.RegisterNotificationCriterionPort;
import com.alzzaipo.hexagonal.notification.domain.NotificationCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterNotificationCriterionService implements RegisterNotificationCriterionUseCase {

    private final RegisterNotificationCriterionPort registerNotificationCriterionPort;
    private final FindMemberPort findMemberPort;

    @Override
    public void registerNotificationCriterion(RegisterNotificationCriterionCommand command) {
        findMemberPort.findMember(command.getMemberUID())
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        NotificationCriterion notificationCriterion = NotificationCriterion.create(
                command.getMemberUID(),
                command.getMinCompetitionRate(),
                command.getMinLockupRate());

        registerNotificationCriterionPort.registerNotificationCriterion(notificationCriterion);
    }
}

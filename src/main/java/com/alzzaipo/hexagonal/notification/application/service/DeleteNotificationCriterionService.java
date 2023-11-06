package com.alzzaipo.hexagonal.notification.application.service;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.hexagonal.notification.application.port.dto.DeleteNotificationCriterionCommand;
import com.alzzaipo.hexagonal.notification.application.port.in.DeleteNotificationCriterionUseCase;
import com.alzzaipo.hexagonal.notification.application.port.out.DeleteNotificationCriterionPort;
import com.alzzaipo.hexagonal.notification.application.port.out.FindNotificationCriterionPort;
import com.alzzaipo.hexagonal.notification.domain.NotificationCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteNotificationCriterionService implements DeleteNotificationCriterionUseCase {

    private final FindMemberPort findMemberPort;
    private final FindNotificationCriterionPort findNotificationCriterionPort;
    private final DeleteNotificationCriterionPort deleteNotificationCriterionPort;

    @Override
    public void deleteNotificationCriterion(DeleteNotificationCriterionCommand command) {
        validateMemberAndOwnership(command);

        deleteNotificationCriterionPort.deleteNotificationCriterion(command.getNotificationCriterionUID());
    }

    private void validateMemberAndOwnership(DeleteNotificationCriterionCommand command) {
        Uid memberUID = command.getMemberUID();
        Uid notificationCriterionUID = command.getNotificationCriterionUID();

        findMemberPort.findMember(memberUID)
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        NotificationCriterion notificationCriterion = findNotificationCriterionPort.findNotificationCriterion(notificationCriterionUID)
                .orElseThrow(() -> new RuntimeException("알림 기준 조회 실패"));

        if (!memberUID.equals(notificationCriterion.getMemberUID())) {
            throw new RuntimeException("오류: 접근 권한 없음");
        }
    }
}

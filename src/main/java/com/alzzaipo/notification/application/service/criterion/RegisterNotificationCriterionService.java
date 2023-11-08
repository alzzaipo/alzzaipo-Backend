package com.alzzaipo.notification.application.service.criterion;

import com.alzzaipo.common.Uid;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.notification.application.port.dto.RegisterNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.in.criterion.RegisterNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.out.criterion.FindMemberNotificationCriteriaPort;
import com.alzzaipo.notification.application.port.out.criterion.RegisterNotificationCriterionPort;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterNotificationCriterionService implements RegisterNotificationCriterionUseCase {

    @Value("${NOTIFICATION_CRITERIA_LIMIT}")
    private int NOTIFICATION_CRITERIA_LIMIT;

    private final FindMemberPort findMemberPort;
    private final RegisterNotificationCriterionPort registerNotificationCriterionPort;
    private final FindMemberNotificationCriteriaPort findMemberNotificationCriteriaPort;

    @Override
    public void registerNotificationCriterion(RegisterNotificationCriterionCommand command) {
        verifyMemberUidValid(command.getMemberUID());
        verifyNotificationCriteriaLimitReached(command);

        NotificationCriterion notificationCriterion = createNotificationCriterion(command);

        registerNotificationCriterionPort.registerNotificationCriterion(notificationCriterion);
    }

    private void verifyMemberUidValid(Uid memberUID) {
        findMemberPort.findMember(memberUID)
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));
    }

    private void verifyNotificationCriteriaLimitReached(RegisterNotificationCriterionCommand command) {
        int totalCount
                = findMemberNotificationCriteriaPort.findMemberNotificationCriteria(command.getMemberUID()).size();

        if (totalCount >= NOTIFICATION_CRITERIA_LIMIT) {
            throw new RuntimeException("오류 : 알림 기준 최대 개수 도달");
        }
    }

    private NotificationCriterion createNotificationCriterion(RegisterNotificationCriterionCommand command) {
        return NotificationCriterion.create(
                command.getMemberUID(),
                command.getMinCompetitionRate(),
                command.getMinLockupRate());
    }
}

package com.alzzaipo.hexagonal.notification.application.service;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.hexagonal.notification.application.port.dto.NotificationCriterionView;
import com.alzzaipo.hexagonal.notification.application.port.in.FindMemberNotificationCriteriaQuery;
import com.alzzaipo.hexagonal.notification.application.port.out.FindMemberNotificationCriteriaPort;
import com.alzzaipo.hexagonal.notification.domain.NotificationCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FindMemberNotificationCriteriaService implements FindMemberNotificationCriteriaQuery {

    private final FindMemberNotificationCriteriaPort findMemberNotificationCriteriaPort;
    private final FindMemberPort findMemberPort;

    @Override
    public List<NotificationCriterionView> findMemberNotificationCriteria(Uid memberUID) {
        findMemberPort.findMember(memberUID)
                .orElseThrow(() -> new RuntimeException("회원 조회 실패"));

        return findMemberNotificationCriteriaPort.findMemberNotificationCriteria(memberUID)
                .stream()
                .map(this::toViewModel)
                .collect(Collectors.toList());
    }

    private NotificationCriterionView toViewModel(NotificationCriterion notificationCriterion) {
        return new NotificationCriterionView(
                notificationCriterion.getNotificationCriterionUID().get(),
                notificationCriterion.getMinCompetitionRate(),
                notificationCriterion.getMinLockupRate());
    }
}

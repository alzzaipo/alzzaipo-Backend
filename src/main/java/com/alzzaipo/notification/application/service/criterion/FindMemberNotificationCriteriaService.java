package com.alzzaipo.notification.application.service.criterion;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.member.application.port.out.member.FindMemberPort;
import com.alzzaipo.notification.application.port.dto.NotificationCriterionView;
import com.alzzaipo.notification.application.port.in.criterion.FindMemberNotificationCriteriaQuery;
import com.alzzaipo.notification.application.port.out.criterion.FindMemberNotificationCriteriaPort;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패"));

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

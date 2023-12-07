package com.alzzaipo.notification.application.service.criterion;

import com.alzzaipo.common.Uid;
import com.alzzaipo.notification.application.port.dto.NotificationCriterionView;
import com.alzzaipo.notification.application.port.in.criterion.FindMemberNotificationCriteriaQuery;
import com.alzzaipo.notification.application.port.out.criterion.FindMemberNotificationCriteriaPort;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindMemberNotificationCriteriaService implements FindMemberNotificationCriteriaQuery {

	private final FindMemberNotificationCriteriaPort findMemberNotificationCriteriaPort;

	@Override
	public List<NotificationCriterionView> findMemberNotificationCriteria(Uid memberUID) {
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

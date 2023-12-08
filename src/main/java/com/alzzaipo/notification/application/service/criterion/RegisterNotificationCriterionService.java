package com.alzzaipo.notification.application.service.criterion;

import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.notification.application.port.dto.RegisterNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.in.criterion.RegisterNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.out.criterion.FindMemberNotificationCriteriaPort;
import com.alzzaipo.notification.application.port.out.criterion.RegisterNotificationCriterionPort;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterNotificationCriterionService implements RegisterNotificationCriterionUseCase {

	@Value("${NOTIFICATION_CRITERIA_LIMIT}")
	private int NOTIFICATION_CRITERIA_LIMIT;

	private final RegisterNotificationCriterionPort registerNotificationCriterionPort;
	private final FindMemberNotificationCriteriaPort findMemberNotificationCriteriaPort;

	@Override
	public void registerNotificationCriterion(RegisterNotificationCriterionCommand command) {
		verifyNotificationCriteriaLimitReached(command);

		NotificationCriterion notificationCriterion = createNotificationCriterion(command);

		registerNotificationCriterionPort.registerNotificationCriterion(notificationCriterion);
	}

	private void verifyNotificationCriteriaLimitReached(
		RegisterNotificationCriterionCommand command) {
		int totalCount
			= findMemberNotificationCriteriaPort.findMemberNotificationCriteria(
			command.getMemberUID()).size();

		if (totalCount >= NOTIFICATION_CRITERIA_LIMIT) {
			throw new CustomException(HttpStatus.FORBIDDEN, "오류 : 알림 기준 최대 개수 도달");
		}
	}

	private NotificationCriterion createNotificationCriterion(
		RegisterNotificationCriterionCommand command) {
		return NotificationCriterion.create(
			command.getMemberUID(),
			command.getMinCompetitionRate(),
			command.getMinLockupRate());
	}
}

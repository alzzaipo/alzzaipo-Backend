package com.alzzaipo.notification.application.service.criterion;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.notification.application.port.dto.UpdateNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.in.criterion.UpdateNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.out.criterion.FindNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.criterion.UpdateNotificationCriterionPort;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateNotificationCriterionService implements UpdateNotificationCriterionUseCase {

	private final FindNotificationCriterionPort findNotificationCriterionPort;
	private final UpdateNotificationCriterionPort updateNotificationCriterionPort;

	@Override
	public void updateNotificationCriterion(UpdateNotificationCriterionCommand command) {
		validateMemberAndOwnership(command);

		updateNotificationCriterionPort.updateNotificationCriterion(command);
	}

	private void validateMemberAndOwnership(UpdateNotificationCriterionCommand command) {
		Uid memberUID = command.getMemberUID();
		Uid notificationCriterionUID = command.getNotificationCriterionUID();

		NotificationCriterion notificationCriterion = findNotificationCriterionPort.findNotificationCriterion(
				notificationCriterionUID)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "알림 기준 조회 실패"));

		if (!memberUID.equals(notificationCriterion.getMemberUID())) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "오류: 권한 없음");
		}
	}
}

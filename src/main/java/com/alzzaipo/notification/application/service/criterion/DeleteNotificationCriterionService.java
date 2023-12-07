package com.alzzaipo.notification.application.service.criterion;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.notification.application.port.dto.DeleteNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.in.criterion.DeleteNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.out.criterion.DeleteNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.criterion.FindNotificationCriterionPort;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteNotificationCriterionService implements DeleteNotificationCriterionUseCase {

	private final FindNotificationCriterionPort findNotificationCriterionPort;
	private final DeleteNotificationCriterionPort deleteNotificationCriterionPort;

	@Override
	public void deleteNotificationCriterion(DeleteNotificationCriterionCommand command) {
		validateMemberAndOwnership(command);

		deleteNotificationCriterionPort.deleteNotificationCriterion(
			command.getNotificationCriterionUID());
	}

	private void validateMemberAndOwnership(DeleteNotificationCriterionCommand command) {
		Uid memberUID = command.getMemberUID();
		Uid notificationCriterionUID = command.getNotificationCriterionUID();

		NotificationCriterion notificationCriterion =
			findNotificationCriterionPort.findNotificationCriterion(notificationCriterionUID)
				.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "알림 기준 조회 실패"));

		if (!memberUID.equals(notificationCriterion.getMemberUID())) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "오류: 접근 권한 없음");
		}
	}
}

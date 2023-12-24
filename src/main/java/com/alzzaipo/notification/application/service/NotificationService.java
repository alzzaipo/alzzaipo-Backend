package com.alzzaipo.notification.application.service;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.notification.application.port.dto.DeleteNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.dto.NotificationCriterionView;
import com.alzzaipo.notification.application.port.dto.RegisterNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.dto.UpdateNotificationCriterionCommand;
import com.alzzaipo.notification.application.port.in.criterion.DeleteNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.in.criterion.FindMemberNotificationCriteriaQuery;
import com.alzzaipo.notification.application.port.in.criterion.RegisterNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.in.criterion.UpdateNotificationCriterionUseCase;
import com.alzzaipo.notification.application.port.out.criterion.DeleteNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.criterion.FindMemberNotificationCriteriaPort;
import com.alzzaipo.notification.application.port.out.criterion.FindNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.criterion.RegisterNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.criterion.UpdateNotificationCriterionPort;
import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService implements RegisterNotificationCriterionUseCase,
	FindMemberNotificationCriteriaQuery,
	UpdateNotificationCriterionUseCase,
	DeleteNotificationCriterionUseCase {

	@Value("${NOTIFICATION_CRITERIA_LIMIT}")
	private int NOTIFICATION_CRITERIA_LIMIT;

	private final RegisterNotificationCriterionPort registerNotificationCriterionPort;
	private final FindMemberNotificationCriteriaPort findMemberNotificationCriteriaPort;
	private final DeleteNotificationCriterionPort deleteNotificationCriterionPort;
	private final FindNotificationCriterionPort findNotificationCriterionPort;
	private final UpdateNotificationCriterionPort updateNotificationCriterionPort;

	@Override
	public void registerNotificationCriterion(RegisterNotificationCriterionCommand command) {
		verifyNotificationCriteriaLimitReached(command);

		NotificationCriterion notificationCriterion = NotificationCriterion.build(command.getMemberUID(),
			command.getMinCompetitionRate(),
			command.getMinLockupRate());

		registerNotificationCriterionPort.registerNotificationCriterion(notificationCriterion);
	}

	@Override
	public List<NotificationCriterionView> findMemberNotificationCriteria(Uid memberUID) {
		return findMemberNotificationCriteriaPort.findMemberNotificationCriteria(memberUID)
			.stream()
			.map(NotificationCriterionView::build)
			.collect(Collectors.toList());
	}

	@Override
	public void updateNotificationCriterion(UpdateNotificationCriterionCommand command) {
		validateMemberAndOwnership(command);
		updateNotificationCriterionPort.updateNotificationCriterion(command);
	}

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

	private void verifyNotificationCriteriaLimitReached(RegisterNotificationCriterionCommand command) {
		int totalCount = findMemberNotificationCriteriaPort.findMemberNotificationCriteria(command.getMemberUID()).size();

		if (totalCount >= NOTIFICATION_CRITERIA_LIMIT) {
			throw new CustomException(HttpStatus.FORBIDDEN, "오류 : 알림 기준 최대 개수 도달");
		}
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

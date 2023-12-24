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
import com.alzzaipo.notification.application.port.out.criterion.CheckNotificationCriterionOwnershipPort;
import com.alzzaipo.notification.application.port.out.criterion.CountMemberNotificationCriteriaPort;
import com.alzzaipo.notification.application.port.out.criterion.DeleteNotificationCriterionPort;
import com.alzzaipo.notification.application.port.out.criterion.FindMemberNotificationCriteriaPort;
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
	private final UpdateNotificationCriterionPort updateNotificationCriterionPort;
	private final DeleteNotificationCriterionPort deleteNotificationCriterionPort;
	private final CountMemberNotificationCriteriaPort countMemberNotificationCriteriaPort;
	private final CheckNotificationCriterionOwnershipPort checkNotificationCriterionOwnershipPort;

	@Override
	public void registerNotificationCriterion(RegisterNotificationCriterionCommand command) {
		NotificationCriterion notificationCriterion = NotificationCriterion.build(command.getMemberUID(),
			command.getMinCompetitionRate(),
			command.getMinLockupRate());

		checkNotificationCriteriaCapacity(command.getMemberUID());

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
		checkOwnership(command.getMemberUID(), command.getNotificationCriterionUID());
		updateNotificationCriterionPort.updateNotificationCriterion(command);
	}

	@Override
	public void deleteNotificationCriterion(DeleteNotificationCriterionCommand command) {
		checkOwnership(command.getMemberUID(), command.getNotificationCriterionUID());
		deleteNotificationCriterionPort.deleteNotificationCriterion(command.getNotificationCriterionUID());
	}

	private void checkNotificationCriteriaCapacity(Uid memberID) {
		int totalCount = countMemberNotificationCriteriaPort.count(memberID.get());
		if (totalCount >= NOTIFICATION_CRITERIA_LIMIT) {
			throw new CustomException(HttpStatus.FORBIDDEN, "오류 : 최대 개수 초과");
		}
	}

	private void checkOwnership(Uid memberId, Uid notificationCriterionId) {
		if (!checkNotificationCriterionOwnershipPort.checkOwnership(memberId.get(), notificationCriterionId.get())) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "오류: 권한 없음");
		}
	}
}

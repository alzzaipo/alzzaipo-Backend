package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.Id;
import lombok.Getter;

@Getter
public class DeleteNotificationCriterionCommand {

    private final Id memberId;
    private final Id notificationCriterionId;

    public DeleteNotificationCriterionCommand(Id memberId, Id notificationCriterionId) {
        this.memberId = memberId;
        this.notificationCriterionId = notificationCriterionId;
    }
}

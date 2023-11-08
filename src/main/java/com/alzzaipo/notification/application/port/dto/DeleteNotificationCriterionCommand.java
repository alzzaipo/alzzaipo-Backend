package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.Uid;
import lombok.Getter;

@Getter
public class DeleteNotificationCriterionCommand {

    private final Uid memberUID;
    private final Uid notificationCriterionUID;

    public DeleteNotificationCriterionCommand(Uid memberUID, Uid notificationCriterionUID) {
        this.memberUID = memberUID;
        this.notificationCriterionUID = notificationCriterionUID;
    }
}

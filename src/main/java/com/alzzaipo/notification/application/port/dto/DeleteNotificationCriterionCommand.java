package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteNotificationCriterionCommand {

    private final Id memberId;
    private final Id notificationCriterionId;
}

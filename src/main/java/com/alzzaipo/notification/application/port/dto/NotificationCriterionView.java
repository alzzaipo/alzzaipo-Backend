package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import lombok.Getter;

@Getter
public class NotificationCriterionView {

    private final String id;
    private final int competitionRate;
    private final int lockupRate;

    private NotificationCriterionView(String id, int competitionRate, int lockupRate) {
        this.id = id;
        this.competitionRate = competitionRate;
        this.lockupRate = lockupRate;
    }

    public static NotificationCriterionView build(NotificationCriterion notificationCriterion) {
        return new NotificationCriterionView(notificationCriterion.getNotificationCriterionId().toString(),
            notificationCriterion.getMinCompetitionRate(),
            notificationCriterion.getMinLockupRate());
    }
}

package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.notification.domain.criterion.NotificationCriterion;
import lombok.Getter;

@Getter
public class NotificationCriterionView {

    private final String uid;
    private final int competitionRate;
    private final int lockupRate;

    private NotificationCriterionView(String uid, int competitionRate, int lockupRate) {
        this.uid = uid;
        this.competitionRate = competitionRate;
        this.lockupRate = lockupRate;
    }

    public static NotificationCriterionView build(NotificationCriterion notificationCriterion) {
        return new NotificationCriterionView(notificationCriterion.getNotificationCriterionUID().toString(),
            notificationCriterion.getMinCompetitionRate(),
            notificationCriterion.getMinLockupRate());
    }
}

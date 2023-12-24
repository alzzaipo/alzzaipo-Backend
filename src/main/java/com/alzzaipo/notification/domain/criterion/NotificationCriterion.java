package com.alzzaipo.notification.domain.criterion;

import com.alzzaipo.common.Uid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationCriterion {

    private final Uid notificationCriterionUID;
    private final Uid memberUID;
    private int minCompetitionRate;
    private int minLockupRate;

    public NotificationCriterion(Uid notificationCriterionUID, Uid memberUID, int minCompetitionRate, int minLockupRate) {
        this.notificationCriterionUID = notificationCriterionUID;
        this.memberUID = memberUID;
        this.minCompetitionRate = minCompetitionRate;
        this.minLockupRate = minLockupRate;
    }

    public static NotificationCriterion create(Uid memberUID, int minCompetitionRate, int minLockupRate) {
        return new NotificationCriterion(Uid.generate(),
                memberUID,
                minCompetitionRate,
                minLockupRate);
    }
}

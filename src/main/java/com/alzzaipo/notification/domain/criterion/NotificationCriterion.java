package com.alzzaipo.notification.domain.criterion;

import com.alzzaipo.common.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationCriterion {

    private final Id notificationCriterionId;
    private final Id memberId;
    private int minCompetitionRate;
    private int minLockupRate;

    public NotificationCriterion(Id notificationCriterionId, Id memberId, int minCompetitionRate, int minLockupRate) {
        this.notificationCriterionId = notificationCriterionId;
        this.memberId = memberId;
        this.minCompetitionRate = minCompetitionRate;
        this.minLockupRate = minLockupRate;
    }

    public static NotificationCriterion build(Id memberId, int minCompetitionRate, int minLockupRate) {
        return new NotificationCriterion(Id.generate(),
            memberId,
                minCompetitionRate,
                minLockupRate);
    }
}

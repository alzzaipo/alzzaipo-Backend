package com.alzzaipo.hexagonal.notification.domain;

import com.alzzaipo.hexagonal.common.Uid;
import com.alzzaipo.hexagonal.common.util.UidGenerator;
import lombok.Getter;

@Getter
public class NotificationCriteria {

    private final Uid uid;
    private final Uid memberUID;
    private int minCompetitionRate;
    private int minLockupRate;

    public NotificationCriteria(Uid uid, Uid memberUID, int minCompetitionRate, int minLockupRate) {
        this.uid = uid;
        this.memberUID = memberUID;
        this.minCompetitionRate = minCompetitionRate;
        this.minLockupRate = minLockupRate;
    }

    public static NotificationCriteria create(Uid memberUID, int minCompetitionRate, int minLockupRate) {
        return new NotificationCriteria(
                UidGenerator.generate(),
                memberUID,
                minCompetitionRate,
                minLockupRate);
    }
}

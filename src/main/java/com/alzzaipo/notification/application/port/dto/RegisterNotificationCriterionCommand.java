package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.Uid;
import lombok.Getter;

@Getter
public class RegisterNotificationCriterionCommand {

    private final Uid memberUID;
    private final int minCompetitionRate;
    private final int minLockupRate;

    public RegisterNotificationCriterionCommand(Uid memberUID, int minCompetitionRate, int minLockupRate) {
        this.memberUID = memberUID;
        this.minCompetitionRate = minCompetitionRate;
        this.minLockupRate = minLockupRate;

        selfValidate();
    }

    private void selfValidate() {
        if (minCompetitionRate < 0 || minCompetitionRate > 10000) {
            throw new IllegalArgumentException("최소 경쟁률 오류 : 0 이상 10000 이하");
        }

        if (minLockupRate < 0 || minLockupRate > 100) {
            throw new IllegalArgumentException("최소 확약비율 오류 : 0 이상 100 이하");
        }
    }
}

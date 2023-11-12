package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.Uid;
import com.alzzaipo.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UpdateNotificationCriterionCommand {

    private final Uid memberUID;
    private final Uid notificationCriterionUID;
    private final int minCompetitionRate;
    private final int minLockupRate;

    public UpdateNotificationCriterionCommand(Uid memberUID, Uid notificationCriterionUID, int minCompetitionRate, int minLockupRate) {
        this.memberUID = memberUID;
        this.notificationCriterionUID = notificationCriterionUID;
        this.minCompetitionRate = minCompetitionRate;
        this.minLockupRate = minLockupRate;

        selfValidate();
    }

    private void selfValidate() {
        if (minCompetitionRate < 0 || minCompetitionRate > 10000) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "최소 경쟁률 오류 : 0 이상 10000 이하");
        }

        if (minLockupRate < 0 || minLockupRate > 100) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "최소 확약비율 오류 : 0 이상 100 이하");
        }
    }
}

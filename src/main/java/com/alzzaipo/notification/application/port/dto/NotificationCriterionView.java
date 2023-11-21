package com.alzzaipo.notification.application.port.dto;

import com.alzzaipo.common.TsidUtil;
import lombok.Getter;

@Getter
public class NotificationCriterionView {

    private final String uid;
    private final int competitionRate;
    private final int lockupRate;

    public NotificationCriterionView(Long uid, int competitionRate, int lockupRate) {
        this.uid = TsidUtil.toString(uid);
        this.competitionRate = competitionRate;
        this.lockupRate = lockupRate;
    }
}

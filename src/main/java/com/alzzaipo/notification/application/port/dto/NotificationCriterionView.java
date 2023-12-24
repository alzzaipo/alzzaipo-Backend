package com.alzzaipo.notification.application.port.dto;

import lombok.Getter;

@Getter
public class NotificationCriterionView {

    private final String uid;
    private final int competitionRate;
    private final int lockupRate;

    public NotificationCriterionView(Long uid, int competitionRate, int lockupRate) {
        this.uid = uid.toString();
        this.competitionRate = competitionRate;
        this.lockupRate = lockupRate;
    }
}

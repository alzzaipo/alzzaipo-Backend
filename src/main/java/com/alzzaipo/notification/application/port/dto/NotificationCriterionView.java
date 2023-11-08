package com.alzzaipo.notification.application.port.dto;

import lombok.Getter;

@Getter
public class NotificationCriterionView {

    private Long uid;
    private int competitionRate;
    private int lockupRate;

    public NotificationCriterionView(Long uid, int competitionRate, int lockupRate) {
        this.uid = uid;
        this.competitionRate = competitionRate;
        this.lockupRate = lockupRate;
    }
}

package com.alzzaipo.hexagonal.notification.application.port.dto;

import lombok.Getter;

@Getter
public class EmailNotificationStatus {

    private final boolean subscriptionStatus;
    private final String email;

    public EmailNotificationStatus(boolean subscriptionStatus, String email) {
        this.subscriptionStatus = subscriptionStatus;
        this.email = email;
    }
}

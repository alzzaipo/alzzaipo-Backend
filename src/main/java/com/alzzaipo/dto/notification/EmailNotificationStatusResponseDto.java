package com.alzzaipo.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationStatusResponseDto {
    private boolean subscriptionStatus;
    private String email;
}

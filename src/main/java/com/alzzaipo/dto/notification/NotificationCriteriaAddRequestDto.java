package com.alzzaipo.dto.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NotificationCriteriaAddRequestDto {
    private int competitionRate;
    private int lockupRate;
}
